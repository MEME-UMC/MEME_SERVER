package org.meme.auth.common;

public interface BaseErrorCode {
    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
