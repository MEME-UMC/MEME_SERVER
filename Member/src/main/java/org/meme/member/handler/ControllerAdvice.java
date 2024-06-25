package org.meme.member.handler;

import org.meme.member.common.BaseResponseDto;
import org.meme.member.common.status.ErrorStatus;
import org.meme.member.exception.AuthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = AuthException.class)
    public BaseResponseDto<?> invokeError(AuthException e) {
        return BaseResponseDto.ErrorResponse((ErrorStatus) e.getBaseErrorCode());
    }
}
