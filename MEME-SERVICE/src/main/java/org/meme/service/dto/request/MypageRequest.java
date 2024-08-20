package org.meme.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.*;

import java.util.List;

public class MypageRequest {

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
        private WorkExperience workExperience;
        private List<Region> region;
        private List<Category> specialization;
        private MakeupLocation makeupLocation;
        private String shopLocation;
    }

    @Builder @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryDto {
        private Long userId;
        private String inquiryTitle;
        private String email;
        private String inquiryText;
    }

}
