package org.meme.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meme.auth.common.BaseResponseDto;
import org.meme.auth.common.exception.AuthException;
import org.meme.auth.common.status.ErrorStatus;
import org.meme.auth.common.status.SuccessStatus;
import org.meme.auth.dto.AuthRequest;
import org.meme.auth.dto.AuthResponse;
import org.meme.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.meme.auth.common.BaseResponseDto.SuccessResponse;

@Slf4j(topic = "MEME-AUTH")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2")
public class AuthController {

    private final AuthService authService;

    /**
     * 소셜 로그인
     * @param joinDto
     * @return
     * @throws AuthException
     */
    @PostMapping("/join/social")
    public BaseResponseDto<AuthResponse.JoinDto> socialJoin(@RequestBody AuthRequest.UserJoinDto joinDto) throws AuthException {
        return SuccessResponse(SuccessStatus.USER_SIGNUP_SUCCESS, authService.socialJoin(joinDto));
    }

    /**
     * 토큰 재발급 컨트롤러입니다.
     *
     * @param reissueDto
     * @return
     * @throws AuthException
     */
    @PostMapping("/reissue")
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
    @PostMapping("/auth/logout")
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
    @PostMapping("/auth/withdraw")
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
    @PostMapping("/check/user")
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
    @PostMapping("/check/nickname")
    public BaseResponseDto<?> checkNicknameDuplicate(@RequestBody AuthRequest.NicknameDto nicknameDto) {
        boolean nicknameIsDuplicate = authService.checkNicknameDuplicate(nicknameDto);
        if (nicknameIsDuplicate)
            return BaseResponseDto.ErrorResponse(ErrorStatus.NICKNAME_NOT_EXIST, true);
        else
            return BaseResponseDto.SuccessResponse(SuccessStatus.NICKNAME_NOT_EXISTS, false);
    }
}
