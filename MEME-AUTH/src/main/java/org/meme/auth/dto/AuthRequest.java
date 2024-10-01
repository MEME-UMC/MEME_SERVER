package org.meme.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.auth.domain.*;

public class AuthRequest {
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelJoinDto {
        private String id_token;
        private Provider provider;
        private String deviceToken;

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
        private String deviceToken;

        private String profile_img;
        private String username;
        private String nickname;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserJoinDto {
        // 초기 사용자 정보
        private String id_token;
        private Provider provider;

        // 사용자 역할
        private Role role;

        // 사용자 세부 정보
        private Gender gender;
        private String username;
        private String nickname;
        private String profileImg;
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
