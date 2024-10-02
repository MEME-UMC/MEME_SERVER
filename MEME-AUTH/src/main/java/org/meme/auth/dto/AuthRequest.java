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
    public static class UserJoinDto {
        // 초기 사용자 정보
        private String idToken;
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
        private String accessToken;
        private String refreshToken;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdTokenDto {
        private String idToken;
        private Provider provider;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NicknameDto {
        private String nickname;
    }
}
