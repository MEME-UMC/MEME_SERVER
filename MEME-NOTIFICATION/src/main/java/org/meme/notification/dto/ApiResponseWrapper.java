package org.meme.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseWrapper<T> {
    private T result;
    private int resultCode;
    private String resultMsg;
}
