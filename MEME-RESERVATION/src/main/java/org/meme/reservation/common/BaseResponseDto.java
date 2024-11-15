package org.meme.reservation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meme.reservation.common.status.ErrorStatus;
import org.meme.reservation.common.status.SuccessStatus;


@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "result", "message", "data"})
public class BaseResponseDto<T> {

    private final int code;
    private final String result;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    // 성공한 경우 응답 생성
    public static <T>BaseResponseDto<T> SuccessResponse(SuccessStatus status, T data) {
        return new BaseResponseDto<>(status.getCode(), "SUCCESS", status.getMessage(), data);
    }

    public static BaseResponseDto SuccessResponse(SuccessStatus status) {
        return new BaseResponseDto<>(status.getCode(), "SUCCESS", status.getMessage(), null);
    }

    public static <T>BaseResponseDto<T> ErrorResponse(ErrorStatus status, T data) {
        return new BaseResponseDto<>(status.getCode(), "FAILURE", status.getMessage(), data);
    }

    public static BaseResponseDto ErrorResponse(ErrorStatus status) {
        return new BaseResponseDto<>(status.getCode(), "FAILURE", status.getMessage(), null);
    }
}
