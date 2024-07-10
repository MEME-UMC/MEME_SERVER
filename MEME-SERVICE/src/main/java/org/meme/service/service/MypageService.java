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
    public void updateModelProfile(MypageRequest.ModelProfileDto request){
        Model model = modelRepository.findById(request.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));

        model.updateModel(request);
    }

    //모델 프로필 관리 조회
    public MypageResponse.ModelProfileDto getModelProfile(Long userId){
        Model model = modelRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
        return MypageConverter.toModelProfileDto(model);
    }

    //아티스트 프로필 관리/수정
    @Transactional
    public void updateArtistProfile(MypageRequest.ArtistProfileDto profileDto){
        Artist artist = artistRepository.findById(profileDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
        artist.updateArtist(profileDto);
    }

    //아티스트 프로필 조회 (관리 조회 용)
    public MypageResponse.ArtistProfileDto getArtistProfile(Long userId){
        Artist artist = artistRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
        return MypageConverter.toArtistProfileDto(artist);
    }

    // 마이페이지 조회
    @Transactional
    public MypageResponse.MypageDetailDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_USER));
        return MypageConverter.toMypageDetailDto(user);
    }

    //문의하기
    @Transactional
    public void createInquiry(MypageRequest.InquiryDto inquiryDto) {
        User user = userRepository.findById(inquiryDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_USER));

        Inquiry inquiry = MypageConverter.toInquiry(inquiryDto, user);
        user.updateInquiryList(inquiry);
        inquiryRepository.save(inquiry);
    }

    // 문의하기 조회
    @Transactional
    public List<MypageResponse.InquiryDto> getInquiry(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_USER));

        List<Inquiry> inquiryList = user.getInquiryList();
        return inquiryList.stream()
                .map(MypageConverter::toInquiryDto)
                .toList();
    }
}
