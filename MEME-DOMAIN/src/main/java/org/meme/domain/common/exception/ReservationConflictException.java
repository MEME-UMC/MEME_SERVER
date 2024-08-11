package org.meme.domain.common.exception;

import org.meme.domain.common.BaseErrorCode;

public class ReservationConflictException extends GeneralException {
    public ReservationConflictException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
