package org.meme.auth.infra;

import lombok.RequiredArgsConstructor;
import org.meme.auth.oauth.jsonwebkey.PublicKeyDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final StringRedisTemplate redisTemplate;
    private static final long CACHING_EXPIRES = 60 * 60 * 24;  // 1일

    public void save(PublicKeyDto publicKeyDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(publicKeyDto.getProvider(), publicKeyDto.getKey());
        redisTemplate.expire(publicKeyDto.getProvider(), CACHING_EXPIRES, TimeUnit.SECONDS);
    }

    public Optional<PublicKeyDto> findPublicKey(String provider) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = valueOperations.get(provider);

        return Optional.of(new PublicKeyDto(provider, key));
    }
}
