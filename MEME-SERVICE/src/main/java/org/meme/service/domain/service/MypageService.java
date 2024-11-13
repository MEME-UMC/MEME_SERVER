package org.meme.service.domain.service;

import lombok.RequiredArgsConstructor;
import org.meme.service.common.exception.GeneralException;
import org.meme.service.common.status.ErrorStatus;
import org.meme.service.domain.entity.Artist;
import org.meme.service.domain.entity.Inquiry;
import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.User;
import org.meme.service.domain.repository.ArtistRepository;
import org.meme.service.domain.repository.InquiryRepository;
import org.meme.service.domain.repository.ModelRepository;
import org.meme.service.domain.repository.UserRepository;
import org.meme.service.domain.converter.MypageConverter;
import org.meme.service.domain.dto.request.MypageRequest;
import org.meme.service.domain.dto.response.MypageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageService {
    private final ModelRepository modelRepository;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    //모델 프로필 관리
    @Transactional
    public void updateModelProfile(MypageRequest.ModelProfileDto request) {
        Model model = findModelById(request.getUserId());
        updateModelEntity(model, request);
    }

    //모델 프로필 관리 조회
    public MypageResponse.ModelProfileDto getModelProfile(Long userId) {
        Model model = findModelById(userId);
        return MypageConverter.toModelProfileDto(model);
    }

    //아티스트 프로필 관리/수정
    @Transactional
    public void updateArtistProfile(MypageRequest.ArtistProfileDto profileDto) {
        Artist artist = findArtistById(profileDto.getUserId());
        updateArtistEntity(artist, profileDto);
    }

    //아티스트 프로필 조회 (관리 조회 용)
    public MypageResponse.ArtistProfileDto getArtistProfile(Long userId) {
        Artist artist = findArtistById(userId);
        return MypageConverter.toArtistProfileDto(artist);
    }

    // 마이페이지 조회
    @Transactional
    public MypageResponse.MypageDetailDto getProfile(Long userId) {
        User user = findUserById(userId);
        return MypageConverter.toMypageDetailDto(user);
    }

    //문의하기
    @Transactional
    public void createInquiry(MypageRequest.InquiryDto inquiryDto) {
        User user = findUserById(inquiryDto.getUserId());
        Inquiry inquiry = MypageConverter.toInquiry(inquiryDto, user);

        user.updateInquiryList(inquiry);
        inquiryRepository.save(inquiry);
    }

    // 문의하기 조회
    @Transactional
    public List<MypageResponse.InquiryDto> getInquiry(Long userId) {
        User user = findUserById(userId);

        List<Inquiry> inquiryList = user.getInquiryList();
        return inquiryList.stream()
                .map(MypageConverter::toInquiryDto)
                .toList();
    }

    private void updateModelEntity(Model model, MypageRequest.ModelProfileDto request) {
        model.getUser().updateProfileImg(request.getProfileImg());
        model.getUser().updateNickname(request.getNickname());
        model.getUser().updateGender(request.getGender());
        model.updateSkinType(request.getSkinType());
        model.updatePersonalColor(request.getPersonalColor());
    }

    private void updateArtistEntity(Artist artist, MypageRequest.ArtistProfileDto request) {
        artist.getUser().updateProfileImg(request.getProfileImg());
        artist.getUser().updateNickname(request.getNickname());
        artist.getUser().updateGender(request.getGender());

        validIntroductionLength(request.getIntroduction());
        artist.updateIntroduction(request.getIntroduction());
        artist.updateWorkExperience(request.getWorkExperience());
        artist.updateRegion(request.getRegion());
        artist.updateSpecialization(request.getSpecialization());
        artist.updateMakeupLocation(request.getMakeupLocation());
        artist.updateShopLocation(request.getShopLocation());
    }

    private void validIntroductionLength(String introduction) {
        if (introduction != null && introduction.length() > 500) {
            throw new GeneralException(ErrorStatus.OVERFLOW_ARTIST_INTRODUCTION);
        }
    }

    private User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_USER));
    }

    private Artist findArtistById(Long artistId){
        return artistRepository.findArtistByUserId(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
    }

    private Model findModelById(Long modelId){
        return modelRepository.findModelByUserId(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
    }
}
