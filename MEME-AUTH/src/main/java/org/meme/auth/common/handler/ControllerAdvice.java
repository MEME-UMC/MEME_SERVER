package org.meme.auth.common.handler;

import org.meme.auth.common.BaseResponseDto;
import org.meme.auth.common.exception.AuthException;
import org.meme.auth.common.exception.ReservationException;
import org.meme.auth.common.status.ErrorStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = AuthException.class)
    public BaseResponseDto<?> invokeError(AuthException e) {
        return BaseResponseDto.ErrorResponse((ErrorStatus) e.getBaseErrorCode());
    }

    @ExceptionHandler(value = ReservationException.class)
    public BaseResponseDto<?> invokeError(ReservationException e) {
        return BaseResponseDto.ErrorResponse((ErrorStatus) e.getBaseErrorCode());
    }
}
