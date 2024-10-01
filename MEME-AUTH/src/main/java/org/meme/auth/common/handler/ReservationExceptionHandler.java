package org.meme.auth.common.handler;

import org.meme.auth.common.BaseResponseDto;
import org.meme.auth.common.exception.ReservationConflictException;
import org.meme.auth.common.exception.ReservationException;
import org.meme.auth.common.status.ErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ReservationExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(value = ReservationConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public BaseResponseDto<?> handleReservationConflictException(ReservationConflictException exception) {
        return BaseResponseDto.ErrorResponse(ErrorStatus.RESERVATION_CONFLICT);
    }

    @ExceptionHandler(value = ReservationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponseDto<?> handleReservationException(ReservationException exception) {
        return BaseResponseDto.ErrorResponse(ErrorStatus.RESERVATION_CANNOT_ACQUIRE_LOCK);
    }

    @ExceptionHandler(value = ReservationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponseDto<?> handleReservationExpection(ReservationException exception) {
        return BaseResponseDto.ErrorResponse(ErrorStatus.RESERVATION_NOT_FOUND);
    }
}
