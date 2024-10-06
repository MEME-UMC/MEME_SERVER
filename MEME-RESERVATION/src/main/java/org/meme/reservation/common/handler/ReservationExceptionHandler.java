package org.meme.reservation.common.handler;

import org.meme.reservation.common.BaseResponseDto;
import org.meme.reservation.common.exception.ReservationConflictException;
import org.meme.reservation.common.exception.ReservationException;
import org.meme.reservation.common.status.ErrorStatus;
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
