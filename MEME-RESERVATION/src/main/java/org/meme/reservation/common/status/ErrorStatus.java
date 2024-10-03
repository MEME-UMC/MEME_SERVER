package org.meme.reservation.common.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meme.reservation.common.BaseErrorCode;
import org.meme.reservation.common.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    //일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,400,"잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,401,"인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, 403, "금지된 요청입니다."),

    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 401, "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, 402, "닉네임은 필수 입니다."),
    NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, 402, "중복된 닉네임입니다."),

    // Artist 관련 에러
    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, 401, "Artist 정보가 존재하지 않습니다."),

    //Model 관련 에러
    MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, 401, "Model 정보가 존재하지 않습니다."),

    //Oauth Error
    KAKAO_ACCESS_TOKEN_ERROR(HttpStatus.NOT_FOUND, 401, "Access Token이 존재하지 않습니다"),

    //JWT 토큰 관련 에러
    JWT_BAD_REQUEST(HttpStatus.BAD_REQUEST,400,"잘못된 JWT 서명입니다."),
    JWT_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, 401,"유효한 JWT 토큰이 없습니다"),
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,402,"액세스 토큰이 만료되었습니다"),
    JWT_TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED,403,"지원하지 않는 JWT 토큰입니다"),

    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, 401, "올바르지 않은 JWT 토큰입니다."),
    JWT_PAYLOAD_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "Payload 정보가 일치하지 않습니다."),
    JWT_EXPIRED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "JWT 토큰이 만료되었습니다."),

    NO_PUBLIC_KEY_EXCEPTION(HttpStatus.NOT_FOUND, 401, "일치하는 공개 키가 없습니다."),
    TOKEN_MISMATCH_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "사용자가 일치하지 않습니다"),
    INVALID_SIGNATURE_EXCEPTION(HttpStatus.BAD_REQUEST,400,"잘못된 JWT 서명입니다."),

    CANNOT_REISSUE_JWT_TOKEN(HttpStatus.BAD_REQUEST, 404, "토큰 재발급을 진행할 수 없습니다."),

    // 에러 모음
    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "지원하지 않은 JWT 입니다."),
    MALFORMED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "올바르지 않은 JWT 입니다."),
    SIGNATURE_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "JWT 토큰 서명 오류입니다."),
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "만료된 JWT 토큰 입니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "올바르지 않은 JWT 입니다."),
    GENERAL_SECURITY_EXCEPTION(HttpStatus.UNAUTHORIZED, 401, "공개 키 서명 오류입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "요청한 주소로 응답을 받을 수 없습니다."),

    PROVIDER_ERROR(HttpStatus.UNAUTHORIZED, 401, "지정한 소셜 로그인 방식을 찾을 수 없습니다."),
    KEY_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "일치하는 Web Key를 찾을 수 없습니다."),

    CANNOT_FOUND_USER(HttpStatus.UNAUTHORIZED, 401, "등록된 사용자가 아닙니다."),
    NO_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 401, "Refresh Token이 없습니다."),
    ANOTHER_USER(HttpStatus.UNAUTHORIZED, 401, "토큰 탈취 위협이 있습니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, 401, "사용자를 찾을 수 없습니다."),

    // Reservation Error
    RESERVATION_CONFLICT(HttpStatus.CONFLICT, 409, "이미 존재하는 예약입니다."),
    RESERVATION_CANNOT_ACQUIRE_LOCK(HttpStatus.FORBIDDEN, 403, "접근이 불가합니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "예약 정보를 가져올 수 없습니다."),

    /**
     * Code : 400
     * Bad Request
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, 400,  "유효하지 않은 요청입니다."),
    ALREADY_EXIST_FAVORITE_ARTIST(HttpStatus.BAD_REQUEST, 400, "해당 아티스트는 이미 관심 아티스트로 등록되어있습니다."),
    ALREADY_EXIST_FAVORITE_PORTFOLIO(HttpStatus.BAD_REQUEST, 400, "해당 포트폴리오는 이미 관심 포트폴리오로 등록되어있습니다."),

    //portfolio
    ALREADY_EXIST_PORTFOLIO(HttpStatus.BAD_REQUEST, 400, "해당 포트폴리오 제목이 이미 존재합니다"),
    INVALID_SORT_CRITERIA(HttpStatus.BAD_REQUEST, 400, "잘못된 정렬 기준입니다"),
    BLOCKED_PORTFOLIO(HttpStatus.BAD_REQUEST, 400, "숨김 처리된 포트폴리오입니다"),
    NOT_EXIST_PORTFOLIO_IMG(HttpStatus.BAD_REQUEST, 400, "포트폴리오 이미지를 찾을 수 없습니다."),

    //review
    ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, 400, "이미 리뷰 작성이 완료된 예약입니다."),
    INVALID_REVIEW_REQUEST(HttpStatus.BAD_REQUEST, 400, "예약이 완료되지 않아 리뷰를 작성할 수 없습니다."),
    INVALID_MODEL_FOR_REVIEW(HttpStatus.BAD_REQUEST, 400,  "사용자가 작성하지 않은 리뷰입니다."),

    //reservation
    INVAILD_AVAILABLE_TIME(HttpStatus.BAD_REQUEST, 400, "잘못된 예약 시간입니다."),
    ALREADY_CHANGE_STATUS(HttpStatus.BAD_REQUEST, 400, "이미 예약 상태가 변경되었습니다."),
    INVALID_CHANGE_STATUS(HttpStatus.BAD_REQUEST, 400, "이미 완료된 예약은 취소할 수 없습니다."),
    NOT_ALLOW_DUPLICATED_RESERVATION(HttpStatus.BAD_REQUEST, 400, "중복된 예약 시간입니다."),

    NOT_ALLOW_OVER_ONE_RESERVATION(HttpStatus.BAD_REQUEST, 400,"한 번에 하나의 예약만 가능합니다." ),
    INVALID_CHANGE_COMPLETE(HttpStatus.BAD_REQUEST, 400,"취소된 예약을 완료 상태로 변경할 수 없습니다." ),


    /**
     * Code : 403
     * Not Authorized
     */

    //portfoilo
    NOT_AUTHORIZED_PORTFOLIO(HttpStatus.UNAUTHORIZED ,403, "아티스트가 해당 포트폴리오에 수정 권한이 없습니다."),



    /**
     * Code : 404
     * Not Found
     */
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, 404, "존재하지 않는 유저입니다."),
    NOT_EXIST_MODEL(HttpStatus.NOT_FOUND, 404, "존재하지 않는 모델입니다."),
    NOT_EXIST_ARTIST(HttpStatus.NOT_FOUND, 404, "존재하지 않는 아티스트입니다."),
    NOT_EXIST_PORTFOLIO(HttpStatus.NOT_FOUND, 404, "존재하지 않는 포트폴리오입니다."),
    NOT_EXIST_RESERVATION(HttpStatus.NOT_FOUND, 404, "존재하지 않는 예약입니다."),
    NOT_EXIST_FAVORITE_ARTIST(HttpStatus.NOT_FOUND, 404, "존재하지 않는 관심 아티스트입니다."),
    NOT_EXIST_FAVORITE_PORTFOLIO(HttpStatus.NOT_FOUND, 404, "존재하지 않는 관심 메이크업입니다."),
    NOT_EXIST_REVIEW(HttpStatus.NOT_FOUND, 404, "존재하지 않는 리뷰입니다."),
    NOT_EXIST_REVIEW_IMG(HttpStatus.NOT_FOUND, 404, "존재하지 않는 리뷰 이미지입니다."),
    NOT_EXIST_INQUIRY(HttpStatus.NOT_FOUND, 404, "존재하지 않는 문의입니다."),
    SEARCH_NOT_FOUNT(HttpStatus.NOT_FOUND, 404, "검색 결과가 존재하지 않습니다."),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "페이지를 찾을 수 없습니다");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public ErrorReasonDto getReason(){
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .success(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus(){
        return ErrorReasonDto.builder()
                .httpStatus(httpStatus)
                .message(message)
                .code(code)
                .success(false)
                .build();
    }
}