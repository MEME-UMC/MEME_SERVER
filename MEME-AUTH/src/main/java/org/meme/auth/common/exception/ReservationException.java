package org.meme.auth.common.exception;

import org.meme.auth.common.BaseErrorCode;

public class ReservationException extends GeneralException {
    public ReservationException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
