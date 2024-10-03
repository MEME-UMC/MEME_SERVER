package org.meme.reservation.common.exception;

import org.meme.reservation.common.BaseErrorCode;

public class ReservationConflictException extends GeneralException {
    public ReservationConflictException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
