package org.meme.reservation.common.exception;

import org.meme.reservation.common.BaseErrorCode;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
