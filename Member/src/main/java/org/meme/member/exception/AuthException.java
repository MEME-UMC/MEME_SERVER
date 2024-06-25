package org.meme.member.exception;


import org.meme.member.common.BaseErrorCode;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
