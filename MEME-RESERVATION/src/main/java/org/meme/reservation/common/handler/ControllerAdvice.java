package org.meme.reservation.common.handler;

import org.meme.reservation.common.BaseResponseDto;
import org.meme.reservation.common.exception.ReservationException;
import org.meme.reservation.common.status.ErrorStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = ReservationException.class)
    public BaseResponseDto<?> invokeError(ReservationException e) {
        return BaseResponseDto.ErrorResponse((ErrorStatus) e.getBaseErrorCode());
    }
}
