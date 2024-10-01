package org.meme.service.common;

public interface BaseErrorCode {
    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
