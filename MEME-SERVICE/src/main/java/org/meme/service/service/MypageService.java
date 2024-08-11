package org.meme.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.exception.GeneralException;
import org.meme.domain.common.status.ErrorStatus;
import org.meme.domain.entity.Artist;
import org.meme.domain.entity.Inquiry;
import org.meme.domain.entity.Model;
import org.meme.domain.entity.User;
import org.meme.domain.repository.ArtistRepository;
import org.meme.domain.repository.InquiryRepository;
import org.meme.domain.repository.ModelRepository;
import org.meme.domain.repository.UserRepository;
import org.meme.service.converter.MypageConverter;
import org.meme.service.dto.request.MypageRequest;
import org.meme.service.dto.response.MypageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        if (request.getProfileImg() != null)
            model.updateProfileImg(request.getProfileImg());
        if (request.getNickname() != null)
            model.updateNickname(request.getNickname());
        if (request.getGender() != null)
            model.updateGender(request.getGender());
        if (request.getSkinType() != null)
            model.setSkinType(request.getSkinType());
        if (request.getPersonalColor() != null)
            model.setPersonalColor(request.getPersonalColor());
    }

    private void updateArtistEntity(Artist artist, MypageRequest.ArtistProfileDto request) {
        if (request.getProfileImg() != null)
            artist.updateProfileImg(request.getProfileImg());
        if (request.getNickname() != null)
            artist.updateNickname(request.getNickname());
        if (request.getGender() != null)
            artist.updateGender(request.getGender());
        if (request.getIntroduction() != null)
            artist.setIntroduction(request.getIntroduction());
        if (request.getWorkExperience() != null)
            artist.setWorkExperience(request.getWorkExperience());
        if (request.getRegion() != null)
            artist.setRegion(request.getRegion());
        if (request.getSpecialization() != null)
            artist.setSpecialization(request.getSpecialization());
        if (request.getMakeupLocation() != null)
            artist.setMakeupLocation(request.getMakeupLocation());
        if (request.getShopLocation() != null)
            artist.setShopLocation(request.getShopLocation());
    }

    private User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_USER));
    }

    private Artist findArtistById(Long artistId){
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
    }

    private Model findModelById(Long modelId){
        return modelRepository.findById(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
    }
}
