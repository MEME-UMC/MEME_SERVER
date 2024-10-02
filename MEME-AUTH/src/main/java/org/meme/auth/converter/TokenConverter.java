package org.meme.auth.converter;

import org.meme.auth.domain.Role;
import org.meme.auth.domain.User;
import org.meme.auth.dto.AuthResponse;

public class TokenConverter {

    public static AuthResponse.JoinDto toJoinDto(User user, String[] tokenPair, Role role) {
        return AuthResponse.JoinDto.builder()
                .accessToken(tokenPair[0])
                .refreshToken(tokenPair[1])
                .userId(user.getUserId())
                .details(user.isDetails())
                .role(role)
                .build();
    }

    public static AuthResponse.TokenDto toTokenDto(String[] tokenPair) {
        return AuthResponse.TokenDto.builder()
                .access_token(tokenPair[0])
                .refresh_token(tokenPair[1])
                .build();
    }
}
