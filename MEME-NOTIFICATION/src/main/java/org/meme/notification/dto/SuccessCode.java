package org.meme.notification.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    SELECT_SUCCESS(HttpStatus.OK.value(), "Success");

    private final int status;
    private final String message;

    SuccessCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
