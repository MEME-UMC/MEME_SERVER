package org.meme.domain.common.handler;

import org.meme.domain.common.BaseResponseDto;
import org.meme.domain.common.exception.ReservationException;
import org.meme.domain.common.status.ErrorStatus;
import org.meme.domain.common.exception.AuthException;
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
