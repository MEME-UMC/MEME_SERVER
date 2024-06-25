package org.meme.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meme.member.common.BaseErrorCode;
import org.meme.member.common.ErrorReasonDto;


@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode baseErrorCode;

    public ErrorReasonDto getReason(){
        return this.baseErrorCode.getReason();
    }

    public ErrorReasonDto getReasonHttpStatus(){
        return this.baseErrorCode.getReasonHttpStatus();
    }
}
