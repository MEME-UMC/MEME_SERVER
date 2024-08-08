package org.meme.service.converter;

import org.meme.domain.entity.Artist;
import org.meme.domain.entity.Inquiry;
import org.meme.domain.entity.Model;
import org.meme.domain.entity.User;
import org.meme.service.dto.request.MypageRequest;
import org.meme.service.dto.response.MypageResponse;

public class MypageConverter {
    public static MypageResponse.ModelProfileDto toModelProfileDto(Model model){
        return MypageResponse.ModelProfileDto.builder()
                .userId(model.getUserId())
                .profileImg(model.getProfileImg())
                .nickname(model.getNickname())
                .gender(model.getGender())
                .skinType(model.getSkinType())
                .personalColor(model.getPersonalColor())
                .build();
    }

    public static MypageResponse.ArtistProfileDto toArtistProfileDto(Artist artist){
        return MypageResponse.ArtistProfileDto.builder()
                .userId(artist.getUserId())
                .profileImg(artist.getProfileImg())
                .nickname(artist.getNickname())
                .gender(artist.getGender())
                .introduction(artist.getIntroduction())
                .phoneNumber(artist.getPhoneNumber())
                .instagramId(artist.getInstagramId())
                .workExperience(artist.getWorkExperience())
                .region(artist.getRegion())
                .specialization(artist.getSpecialization())
                .makeupLocation(artist.getMakeupLocation())
                .shopLocation(artist.getShopLocation())
                .build();
    }

    public static MypageResponse.MypageDetailDto toMypageDetailDto(User user){
        return MypageResponse.MypageDetailDto.builder()
                .profileImg(user.getProfileImg())
                .nickname(user.getNickname())
                .name(user.getUsername())
                .gender(user.getGender())
                .email(user.getEmail())
                .build();
    }

    // inquiry
    public static Inquiry toInquiry(MypageRequest.InquiryDto dto, User user){
        return Inquiry.builder()
                .inquiryText(dto.getInquiryText())
                .inquiryTitle(dto.getInquiryTitle())
                .user(user)
                .build();
    }

    public static MypageResponse.InquiryDto toInquiryDto(Inquiry inquiry) {
        return MypageResponse.InquiryDto.builder()
                .inquiryText(inquiry.getInquiryText())
                .inquiryTitle(inquiry.getInquiryTitle())
                .userEmail(inquiry.getUser().getEmail())
                .build();
    }

}
