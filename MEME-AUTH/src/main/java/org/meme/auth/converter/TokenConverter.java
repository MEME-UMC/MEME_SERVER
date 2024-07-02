package org.meme.auth.converter;

import org.meme.auth.dto.AuthResponse;
import org.meme.domain.Entity.User;

public class TokenConverter {

    public static AuthResponse.JoinDto toJoinDto(User user, String[] tokenPair, String role) {
        return AuthResponse.JoinDto.builder()
                .access_token(tokenPair[0])
                .refresh_token(tokenPair[1])
                .user_id(user.getUserId())
                .details(user.getDetails())
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
