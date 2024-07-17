package org.meme.domain.repository;

import lombok.RequiredArgsConstructor;
import org.meme.domain.entity.Token;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class TokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRES = 7 * 24 * 60 * 60;

    public void save(Token token) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token.getAccessToken(), token.getRefreshToken());
        redisTemplate.expire(token.getAccessToken(), REFRESH_TOKEN_EXPIRES, TimeUnit.SECONDS);
    }

    public Optional<Token> findByAccessToken(String accessToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(accessToken);

        return Optional.of(new Token(accessToken, refreshToken));
    }

    public void delete(Token token) {
        redisTemplate.delete(token.getAccessToken());
    }
}
