package org.meme.auth.converter;

import org.meme.auth.config.SecurityConfig;
import org.meme.domain.entity.Artist;
import org.meme.auth.dto.AuthRequest;
import org.meme.auth.dto.AuthResponse;
import org.meme.domain.entity.Model;
import org.meme.domain.entity.User;

import java.util.Collections;
import java.util.HashSet;

public class UserConverter {

    public static Model toModel(AuthRequest.ModelJoinDto modelJoinDto, String userEmail, String role) {
        return Model.builder()
                .role(role)
                .email(userEmail)
                .provider(modelJoinDto.getProvider())
                .profileImg(modelJoinDto.getProfile_img())
                .deviceTokens(new HashSet<>(Collections.singleton(modelJoinDto.getDeviceToken())))
                .username(modelJoinDto.getUsername())
                .nickname(modelJoinDto.getNickname())
                .password(SecurityConfig.passwordEncoder().encode(userEmail))
                .details(true)
                .gender(modelJoinDto.getGender())
                .skinType(modelJoinDto.getSkin_type())
                .personalColor(modelJoinDto.getPersonal_color())
                .build();
    }

    public static Artist toArtist(AuthRequest.ArtistJoinDto artistJoinDto, String userEmail, String role) {
        return Artist.builder()
                .role(role)
                .email(userEmail)
                .provider(artistJoinDto.getProvider())
                .profileImg(artistJoinDto.getProfile_img())
                .deviceTokens(new HashSet<>(Collections.singleton(artistJoinDto.getDeviceToken())))
                .username(artistJoinDto.getUsername())
                .nickname(artistJoinDto.getNickname())
                .password(SecurityConfig.passwordEncoder().encode(userEmail))
                .details(false)
                .build();
    }

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
