package org.meme.auth.service;

import lombok.RequiredArgsConstructor;
import org.meme.auth.common.exception.AuthException;
import org.meme.auth.common.status.ErrorStatus;
import org.meme.auth.converter.TokenConverter;
import org.meme.auth.converter.UserConverter;
import org.meme.auth.domain.*;
import org.meme.auth.dto.AuthRequest;
import org.meme.auth.dto.AuthResponse;
import org.meme.auth.infra.RedisRepository;
import org.meme.auth.jwt.JwtTokenProvider;
import org.meme.auth.oauth.provider.OAuthProvider;
import org.meme.auth.oauth.provider.apple.AppleAuthProvider;
import org.meme.auth.oauth.provider.kakao.KakaoAuthProvider;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.meme.auth.domain.Provider.APPLE;
import static org.meme.auth.domain.Provider.KAKAO;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final ModelRepository modelRepository;  // 필수 - 사용자 저장
    private final ArtistRepository artistRepository;  // 필수 - 사용자 저장
    private final AuthenticationManager authenticationManager;  // 필수 - 로그인
    private final JwtTokenProvider jwtTokenProvider;  // 필수 - 토큰 생성
    private final TokenRepository tokenRepository;  // 필수 - 토큰 저장 및 삭제
    private final UserRepository userRepository;  // 필수 - 사용자 정보 조회
    private final RedisRepository redisRepository;  // 필수 - 자식 클래스 의존성 주입 시 필요

    private final static String TOKEN_PREFIX = "Bearer ";
    private static final String USERNAME = "username";
    private final static String ROLE_MODEL = "MODEL";
    private final static String ROLE_ARTIST = "ARTIST";
    private final static int MAX_LENGTH_NICKNAME = 15;

    // updated repository
    private final org.meme.auth.domain.UserRepository updateUserRepository;

    // 새로운 회원가입 메서드
    @Transactional
    public AuthResponse.JoinDto socialJoin(AuthRequest.UserJoinDto signUpDto) {
        // 1. ID 토큰 검증 후, 사용자 이메일 획득
        String userEmail = getUserEmail(signUpDto.getId_token(), signUpDto.getProvider());

        org.meme.auth.domain.User user = saveUser(signUpDto, userEmail);

        Role userRole = signUpDto.getRole();
        // 2. 사용자 엔티티 변환
        if (userRole == Role.ARTIST) {
            // MEMBER_SERVER로 요청보내 ArtistRepository 호출
            // artistRepository.save(new Artist(user)); 처럼 사용
        } else if (userRole == Role.MODEL) {
            // MEMBER_SERVER로 요청보내 ModelRepository 호출
            // modelRepository.save(new Model(user)); 처럼 사용
        } else {
            // 예외 처리
        }

        String[] tokenPair = login(user);

        return TokenConverter.toJoinDto(user, tokenPair, userRole);
    }

    @Transactional
    public AuthResponse.TokenDto reissue(AuthRequest.ReissueDto reissueDto) throws AuthException {
        String requestAccessToken = reissueDto.getAccess_token();
        String requestRefreshToken = reissueDto.getRefresh_token();

        Token requestToken = tokenRepository.findByAccessToken(requestAccessToken)
                .orElseThrow(() -> new AuthException(ErrorStatus.CANNOT_FOUND_USER));

        if (requestToken.getRefreshToken() == null) {
            // Case 1 : refresh token을 가지고 있지 않은 경우
            deleteTokenPairInRedis(requestAccessToken);
            throw new AuthException(ErrorStatus.NO_REFRESH_TOKEN);
        } else if (!requestToken.getRefreshToken().equals(requestRefreshToken)) {
            // Case 2 : access token과 refresh token이 일치하지 않는 경우 -> 토큰 탈취 가능성 존재
            deleteTokenPairInRedis(requestAccessToken);
            throw new AuthException(ErrorStatus.ANOTHER_USER);
        } else {
            // Case 3 : 정상적인 경우
            deleteTokenPairInRedis(requestAccessToken);
            Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
            String[] tokenPair = jwtTokenProvider.createTokenPair(authentication);

            return TokenConverter.toTokenDto(tokenPair);
        }
    }

    @Transactional
    public void logout(String requestHeader) throws AuthException {
        String requestAccessToken = resolveToken(requestHeader);
        deleteTokenPairInRedis(requestAccessToken);
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void withdraw(String requestHeader) throws AuthException {
        String requestAccessToken = resolveToken(requestHeader);
        String username = (String) jwtTokenProvider.getClaims(requestAccessToken).get(USERNAME);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Transactional
    public AuthResponse.UserInfoDto checkUserExistsFindByEmail(AuthRequest.IdTokenDto idTokenDto) {
        String userEmail = getUserEmail(idTokenDto.getId_token(), idTokenDto.getProvider());
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty())
            return UserConverter.toUserInfoDtoNonExists();

        User user = userOptional.get();
        String[] tokenPair = login(user);

        return UserConverter.toUserInfoDtoExists(user, tokenPair);
    }

    @Transactional
    public boolean checkNicknameDuplicate(AuthRequest.NicknameDto nicknameDto) {
        return userRepository.existsByNickname(nicknameDto.getNickname());
    }

    private org.meme.auth.domain.User saveUser(AuthRequest.UserJoinDto signUpDto, String userEmail) {
        return updateUserRepository.save(UserConverter.toUserEntity(signUpDto, userEmail));
    }

    // new
    private String[] login(org.meme.auth.domain.User user) {
        Authentication authentication = authenticate(user);

        String[] tokenPair = jwtTokenProvider.createTokenPair(authentication);
        String accessToken = tokenPair[0];
        String refreshToken = tokenPair[1];
        saveTokenPairInRedis(accessToken, refreshToken);

        return tokenPair;
    }

    // new
    private Authentication authenticate(org.meme.auth.domain.User user) throws AuthException {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getEmail()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException exception) {
            throw new DisabledException("DISABLED_EXCEPTION", exception);
        } catch (LockedException exception) {
            throw new LockedException("LOCKED_EXCEPTION", exception);
        } catch (BadCredentialsException exception) {
            throw new BadCredentialsException("BAD_CREDENTIALS_EXCEPTION", exception);
        }

        return authentication;
    }

    protected String getUserEmail(String idToken, Provider provider) throws AuthException {
        OAuthProvider oAuthProvider;

        if (provider.equals(KAKAO)) {  // Use Kakao OpenID Connect
            oAuthProvider = new KakaoAuthProvider(redisRepository);
        } else if (provider.equals(APPLE)) {  // Use Apple OpenID Connect
            oAuthProvider = new AppleAuthProvider(redisRepository);
        } else {
            throw new AuthException(ErrorStatus.PROVIDER_ERROR);
        }

        return oAuthProvider.getUserEmail(idToken);
    }

    protected void saveTokenPairInRedis(String accessToken, String refreshToken) {
        tokenRepository.save(Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    protected void deleteTokenPairInRedis(String requestAccessToken) throws AuthException {
        Token findToken = tokenRepository.findByAccessToken(requestAccessToken)
                .orElseThrow(() -> new AuthException(ErrorStatus.TOKEN_MISMATCH_EXCEPTION));
        tokenRepository.delete(findToken);
    }

    protected String resolveToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith(TOKEN_PREFIX))
            throw new AuthException(ErrorStatus.JWT_TOKEN_UNSUPPORTED);

        return bearerToken.substring(7);
    }

    protected void checkNicknameLessThanMaxLength(String nickname) throws AuthException {
        if (nickname.length() > MAX_LENGTH_NICKNAME)
            // NICKNAME_LENGTH_EXCEPTION로 바꿔야함!!1
            throw new AuthException(ErrorStatus.NICKNAME_NOT_EXIST);
    }
}
