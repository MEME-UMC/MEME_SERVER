package org.meme.domain.common.exception;

import org.meme.domain.common.BaseErrorCode;

public class ReservationException extends GeneralException {
    public ReservationException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
