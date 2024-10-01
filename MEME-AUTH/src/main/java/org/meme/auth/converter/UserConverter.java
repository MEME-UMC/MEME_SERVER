package org.meme.auth.converter;

import org.meme.auth.config.SecurityConfig;
import org.meme.auth.dto.AuthRequest;
import org.meme.auth.dto.AuthResponse;
import org.meme.auth.domain.User;

public class UserConverter {

    public static org.meme.auth.domain.User toUserEntity(AuthRequest.UserJoinDto signUpDto, String userEmail) {
        return org.meme.auth.domain.User.builder()
                .username(signUpDto.getUsername())
                .email(userEmail)
                .provider(signUpDto.getProvider())
                .password(SecurityConfig.passwordEncoder().encode(userEmail))
                .nickname(signUpDto.getNickname())
                .profileImg(signUpDto.getProfileImg())
                .gender(signUpDto.getGender())
                .role(signUpDto.getRole())
                .details(false)
                .build();
    }

//    public static org.meme.auth.domain.Model toModelEntity() {
//        return org.meme.auth.domain.Model
//                .builder().build();
//    }

//    public static org.meme.auth.domain.Artist toArtistEntity() {
//        return org.meme.auth.domain.Artist
//                .builder().build();
//    }

    public static AuthResponse.UserInfoDto toUserInfoDtoExists(User user, String[] tokenPair) {
        return AuthResponse.UserInfoDto.builder()
                .access_token(tokenPair[0])
                .refresh_token(tokenPair[1])
                .user_status(true)
                .user_id(user.getUserId())
                .role(user.getRole())
                .build();
    }

    public static AuthResponse.UserInfoDto toUserInfoDtoNonExists() {
        return AuthResponse.UserInfoDto.builder()
                .user_status(false)
                .build();
    }
}
