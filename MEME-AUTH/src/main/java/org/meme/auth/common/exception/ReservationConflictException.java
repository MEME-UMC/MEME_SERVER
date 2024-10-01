package org.meme.auth.common.exception;

import org.meme.auth.common.BaseErrorCode;

public class ReservationConflictException extends GeneralException {
    public ReservationConflictException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
