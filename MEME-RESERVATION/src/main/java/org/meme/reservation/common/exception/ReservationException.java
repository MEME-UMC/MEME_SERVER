package org.meme.reservation.common.exception;

import org.meme.reservation.common.BaseErrorCode;

public class ReservationException extends GeneralException {
    public ReservationException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
