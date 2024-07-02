package org.meme.domain.common.exception;

import org.meme.domain.common.BaseErrorCode;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
