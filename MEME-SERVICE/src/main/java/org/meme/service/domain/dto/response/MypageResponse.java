package org.meme.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.*;

import java.util.List;

public class MypageResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelProfileDto {
        private Long userId;
        private String profileImg;
        private String nickname;
        private Gender gender;
        private SkinType skinType;
        private PersonalColor personalColor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistProfileDto {
        private Long userId;
        private String profileImg;
        private String nickname;
        private Gender gender;
        private String introduction;
        private String phoneNumber;
        private String instagramId;
        private WorkExperience workExperience;
        private List<Region> region;
        private List<Category> specialization;
        private MakeupLocation makeupLocation;
        private String shopLocation;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MypageDetailDto {
        private String profileImg;
        private String nickname;
        private String name;
        private Gender gender;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryDto {
        private String userEmail;
        private String inquiryTitle;
        private String inquiryText;
    }

}
