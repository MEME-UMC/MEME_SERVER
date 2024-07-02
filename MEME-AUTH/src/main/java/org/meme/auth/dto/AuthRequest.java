package org.meme.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.*;

import java.util.List;
import java.util.Map;

public class AuthRequest {
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelJoinDto {
        private String id_token;
        private Provider provider;

        private String profile_img;
        private String username;
        private String nickname;

        private Gender gender;
        private SkinType skin_type;
        private PersonalColor personal_color;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistJoinDto {
        private String id_token;
        private Provider provider;

        private String profile_img;
        private String username;
        private String nickname;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistExtraDto {
        private Long user_id;
        private String profile_img;
        private String nickname;
        private Gender gender;
        private String introduction;
        private WorkExperience work_experience;
        private List<Region> region;
        private List<Category> specialization;
        private MakeupLocation makeup_location;
        private String shop_location;
        private Map<DayOfWeek, Times> availableDayOfWeekAndTime;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueDto {
        private String access_token;
        private String refresh_token;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessTokenDto {
        private String access_token;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenDto {
        private String refresh_token;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdTokenDto {
        private String id_token;
        private Provider provider;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NicknameDto {
        private String nickname;
    }
}