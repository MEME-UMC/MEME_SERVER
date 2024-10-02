package org.meme.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.auth.domain.Role;

public class AuthResponse {
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDto {
        private String accessToken;
        private String refreshToken;
        private Long userId;
        private boolean details;  // 이거 좀 생각해보기
        private Role role;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDto {
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
    public static class UserInfoDto {
        private String access_token;
        private String refresh_token;
        private boolean user_status;
        private Long user_id;
        private Role role;
    }
}
