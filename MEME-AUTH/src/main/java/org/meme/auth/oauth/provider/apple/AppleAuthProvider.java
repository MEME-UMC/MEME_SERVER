package org.meme.auth.oauth.provider.apple;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.meme.auth.common.exception.AuthException;
import org.meme.auth.common.status.ErrorStatus;
import org.meme.auth.infra.RedisRepository;
import org.meme.auth.oauth.jsonwebkey.PublicKeyDto;
import org.meme.auth.oauth.provider.OAuthProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppleAuthProvider extends OAuthProvider {

    private static final String REQUEST_URL = "https://appleid.apple.com/auth/keys";
    private static final String PROVIDER = "APPLE";

    @Value("${spring.security.oauth2.apple.issuer}")
    private String issuer;

    @Value("${spring.security.oauth2.apple.client-id}")
    private String restApiKey;

    private final RedisRepository redisRepository;

    public AppleAuthProvider(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Override
    protected String getJsonWebKeys() throws IOException {
        // Redis 안에 캐시 값으로 카카오 OIDC 공개 키 목록이 저장되어 있는지 확인
        Optional<PublicKeyDto> applePublicKeyDto = redisRepository.findPublicKey(PROVIDER);

        // 공개 키 목록이 저장되어 있지 않다면 GET 요청 보내서 공개 키 세팅 (공개 키 캐시 여부 확인)
        if (applePublicKeyDto.get().getKey() == null)
            setPublicKeys();

        // 공개 키 목록이 저장되어 있다면 키 목록 가져오고 파싱 진행
        return redisRepository.findPublicKey(PROVIDER).get().getKey();
    }

    @Override
    protected Claims validateSignature(String idToken, Key signingKey) throws AuthException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .requireIssuer(issuer)
                    .requireAudience(restApiKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();
        } catch (UnsupportedJwtException e) {
            throw new AuthException(ErrorStatus.UNSUPPORTED_JWT_EXCEPTION);
        } catch (MalformedJwtException e) {
            throw new AuthException(ErrorStatus.MALFORMED_JWT_EXCEPTION);
        } catch (SignatureException e) {
            throw new AuthException(ErrorStatus.SIGNATURE_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorStatus.EXPIRED_JWT_EXCEPTION);
        } catch (IllegalArgumentException e) {
            throw new AuthException(ErrorStatus.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    private void setPublicKeys() throws IOException {
        // 공개키 목록 조회 URL 요청
        URL url = new URL(REQUEST_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode >= 200 && responseCode <= 300) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String jsonData = "";
            List<String> keys = new ArrayList<>();

            while ((line = br.readLine()) != null)
                jsonData += line;

            redisRepository.save(PublicKeyDto.builder()
                    .provider(PROVIDER)
                    .key(jsonData)
                    .build());
        } else {
            throw new IOException();
        }
    }
}

