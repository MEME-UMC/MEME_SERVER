package org.meme.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meme.domain.common.BaseResponseDto;
import org.meme.domain.common.status.ErrorStatus;
import org.meme.domain.common.status.SuccessStatus;
import org.meme.auth.service.AuthService;
import org.meme.auth.dto.AuthRequest;
import org.meme.auth.dto.AuthResponse;
import org.meme.domain.common.exception.AuthException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j(topic = "MEME-AUTH")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    /**
     * 모델 회원가입 컨트롤러입니다.
     *
     * @param modelJoinDto
     * @return
     * @throws AuthException
     */
    @PostMapping("/api/v1/signup/model")
    public BaseResponseDto<AuthResponse.JoinDto> signupModel(@RequestBody AuthRequest.ModelJoinDto modelJoinDto) throws AuthException {
        log.info("Sign up Model From: {}", modelJoinDto.getNickname());
        return BaseResponseDto.SuccessResponse(SuccessStatus.USER_JOIN_SUCCESS, authService.signupModel(modelJoinDto));
    }

    /**
     * 아티스트 회원가입 컨트롤러입니다.
     *
     * @param artistJoinDto
     * @return
     * @throws AuthException
     */
    @PostMapping("/api/v1/signup/artist")
    public BaseResponseDto<AuthResponse.JoinDto> signupArtist(@RequestBody AuthRequest.ArtistJoinDto artistJoinDto) throws AuthException {
        return BaseResponseDto.SuccessResponse(SuccessStatus.USER_JOIN_SUCCESS, authService.signupArtist(artistJoinDto));
    }

    /**
     * 아티스트 회원가입 시, 추가 정보를 받는 컨트롤러입니다.
     * 현재 API 중단 상태 -> 추후 리팩토링 예정
     *
     * @param artistExtraDto
     * @return
     * @throws AuthException
     */
//    @PostMapping("/api/v1/auth/artist/extra")
//    public BaseResponseDto<?> signupArtistExtra(@RequestBody AuthRequest.ArtistExtraDto artistExtraDto) throws AuthException {
//        authService.signupArtistExtra(artistExtraDto);
//        return BaseResponseDto.SuccessResponse(SuccessStatus.ARTIST_EXTRA_JOIN_SUCCESS);
//    }

    /**
     * 토큰 재발급 컨트롤러입니다.
     *
     * @param reissueDto
     * @return
     * @throws AuthException
     */
    @PostMapping("/api/v1/reissue")
    public BaseResponseDto<?> reissue(@RequestBody AuthRequest.ReissueDto reissueDto) throws AuthException {
        AuthResponse.TokenDto reissueResult = authService.reissue(reissueDto);

        if (reissueResult.getAccess_token() == null)
            return BaseResponseDto.ErrorResponse(ErrorStatus.CANNOT_REISSUE_JWT_TOKEN);

        return BaseResponseDto.SuccessResponse(SuccessStatus.REISSUE_SUCCESS, reissueResult);
    }

    /**
     * 로그아웃 컨트롤러입니다.
     *
     * @param request
     * @return
     * @throws AuthException
     */
    @PostMapping("/api/v1/auth/logout")
    public BaseResponseDto<?> logout(HttpServletRequest request) throws AuthException {
        authService.logout(request.getHeader("Authorization"));
        return BaseResponseDto.SuccessResponse(SuccessStatus.LOGOUT_SUCCESS);
    }

    /**
     * 회원 탈퇴 컨트롤러입니다.
     *
     * @param request
     * @return
     * @throws AuthException
     */
    @PostMapping("/api/v1/auth/withdraw")
    public BaseResponseDto<?> withdraw(HttpServletRequest request) throws AuthException {
        authService.withdraw(request.getHeader("Authorization"));
        return BaseResponseDto.SuccessResponse(SuccessStatus.WITHDRAW_SUCCESS);
    }

    /**
     * 회원 여부 확인용 컨트롤러입니다.
     *
     * @param idTokenDto
     * @return
     */
    @PostMapping("/api/v1/check/user")
    public BaseResponseDto<?> checkUserExists(@RequestBody AuthRequest.IdTokenDto idTokenDto) {
        AuthResponse.UserInfoDto userInfoDto = authService.checkUserExistsFindByEmail(idTokenDto);
        if (userInfoDto.isUser_status())
            return BaseResponseDto.SuccessResponse(SuccessStatus.USER_EXISTS, userInfoDto);
        else
            return BaseResponseDto.ErrorResponse(ErrorStatus.USER_NOT_FOUND, userInfoDto);
    }

    /**
     * 닉네임 중복 체크 컨트롤러입니다.
     *
     * @param nicknameDto
     * @return
     */
    @PostMapping("/api/v1/check/nickname")
    public BaseResponseDto<?> checkNicknameDuplicate(@RequestBody AuthRequest.NicknameDto nicknameDto) {
        boolean nicknameIsDuplicate = authService.checkNicknameDuplicate(nicknameDto);
        if (nicknameIsDuplicate)
            return BaseResponseDto.ErrorResponse(ErrorStatus.NICKNAME_NOT_EXIST, true);
        else
            return BaseResponseDto.SuccessResponse(SuccessStatus.NICKNAME_NOT_EXISTS, false);
    }
}
