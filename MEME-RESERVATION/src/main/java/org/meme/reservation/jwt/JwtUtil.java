package org.meme.reservation.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@RequiredArgsConstructor
@Service
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public Claims parseToken(String accessToken) {
        Key signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

    public Long extractUserId(String accessToken) {
        System.out.println("accessToken = " + accessToken);
        Claims claims = parseToken(accessToken);
        return claims.get("id", Long.class);
    }
}
