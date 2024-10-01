package org.meme.auth.common.exception;

import org.meme.auth.common.BaseErrorCode;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
