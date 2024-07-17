package org.meme.domain.common.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus {

    // UserController
    USER_JOIN_SUCCESS(HttpStatus.OK, 200, "회원가입이 완료되었습니다"),
    USER_EXISTS(HttpStatus.OK, 200, "사용자 정보가 확인되었습니다."),
    NICKNAME_NOT_EXISTS(HttpStatus.OK, 200, "사용 가능한 닉네임입니다."),

    // AuthController
    LOGIN_SUCCESS(HttpStatus.OK,200,"토큰 검증에 성공하였습니다."),
    REISSUE_SUCCESS(HttpStatus.OK, 200, "토큰 재발급이 완료되었습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, 200, "Logout Success"),
    WITHDRAW_SUCCESS(HttpStatus.OK, 200, "Withdraw Success"),


    // ReservationController
    MAKE_RESERVATION_SUCCESS(HttpStatus.OK, 200, "예약을 완료하였습니다."),
    GET_SCHEDULE_SUCCESS(HttpStatus.OK, 200, "일정 조회를 성공하였습니다."),
    ADD_ENABLE_DATE_SUCCESS(HttpStatus.OK, 200, "영업 가능 날짜 등록에 성공하였습니다."),
    ADD_ENABLE_TIME_SUCCESS(HttpStatus.OK, 200, "영업 가능 시간대 등록에 성공하였습니다."),
    GET_ENABLE_DATE_SUCCESS(HttpStatus.OK, 200, "영업 가능 날짜 조회에 성공하였습니다."),
    GET_ENABLE_TIME_SUCCESS(HttpStatus.OK, 200, "영업 가능 시간대 조회에 성공하였습니다.");

    /**artist**/
    ARTIST_PROFILE_UPDATE(HttpStatus.OK, 200, "아티스트 프로필 수정이 완료되었습니다"),
    ARTIST_PROFILE_GET(HttpStatus.OK,200, "아티스트 프로필 조회가 완료되었습니다"),
    ARTIST_AVAILABLE_TIME_PATCH(HttpStatus.OK,200, "아티스트 예약 가능 시간 편집이 완료되었습니다"),

    PORTFOLIO_CREATE(HttpStatus.OK,200, "포트폴리오 생성이 완료되었습니다"),
    PORTFOLIO_GET(HttpStatus.OK,200, "포트폴리오 조회가 완료되었습니다"),
    PORTFOLIO_UPDATE(HttpStatus.OK,200, "포트폴리오 수정이 완료되었습니다"),

    /**model**/
    MODEL_PROFILE_UPDATE(HttpStatus.OK,200, "모델 프로필 수정이 완료되었습니다"),
    MODEL_PROFILE_GET(HttpStatus.OK,200, "모델 프로필 조회가 완료되었습니다."),

    FAVORITE_ARTIST_GET(HttpStatus.OK,200, "관심 아티스트 조회가 완료되었습니다"),
    FAVORITE_PORTFOLIO_GET(HttpStatus.OK,200, "관심 메이크업 조회가 완료되었습니다"),
    FAVORITE_ARTIST_POST(HttpStatus.OK,200, "관심 아티스트 추가가 완료되었습니다."),
    FAVORITE_PORTFOLIO_POST(HttpStatus.OK,200, "관심 메이크업 추가가 완료되었습니다."),
    FAVORITE_ARTIST_DELETE(HttpStatus.OK,200, "관심 아티스트 삭제가 완료되었습니다."),
    FAVORITE_PORTFOLIO_DELETE(HttpStatus.OK,200, "관심 메이크업 삭제가 완료되었습니다."),

    SEARCH_GET(HttpStatus.OK,200, "조회가 완료되었습니다"),

    RECOMMEND_REVIEW_GET(HttpStatus.OK,200, "리뷰 순 포트폴리오 추천이 완료되었습니다."),
    RECOMMEND_RECENT_GET(HttpStatus.OK,200, "최신 순 포트폴리오 추천이 완료되었습니다."),

    /**review**/
    REVIEW_CREATE(HttpStatus.OK,200, "리뷰 작성이 완료되었습니다"),
    REVIEW_GET(HttpStatus.OK,200, "리뷰 조회가 완료되었습니다"),
    REVIEW_DETAILS_GET(HttpStatus.OK,200, "리뷰 상세 조회가 완료되었습니다"),
    REVIEW_AVAILABLE_GET(HttpStatus.OK,200, "리뷰 작성 가능 예약 리스트 조회가 완료되었습니다"),
    REVIEW_PATCH(HttpStatus.OK,200, "리뷰 수정이 완료되었습니다"),
    REVIEW_DELETE(HttpStatus.OK,200, "리뷰 삭제가 완료되었습니다"),


    /**reservation**/
    RESERVATION_CREATE(HttpStatus.OK,200, "예약이 완료되었습니다"),
    RESERVATION_DETAILS_GET(HttpStatus.OK,200, "예약 상세 조회가 완료되었습니다"),
    RESERVATION_GET(HttpStatus.OK,200, "예약 조회가 완료되었습니다"),
    RESERVATION_UPDATE(HttpStatus.OK,200, "예약 상태 변경이 완료되었습니다"),
    ARTIST_LOCATION_GET(HttpStatus.OK,200, "아티스트의 예약 가능 장소 조회가 완료되었습니다"),
    ARTIST_TIME_GET(HttpStatus.OK,200, "아티스트의 예약 가능 시간 조회가 완료되었습니다."),

    /**mypage**/
    MYPAGE_GET(HttpStatus.OK,200, "마이페이지 조회가 완료되었습니다"),
    DETAILS_GET(HttpStatus.OK,200, "내 정보 조회가 완료되었습니다"),
    TOS_GET(HttpStatus.OK,200, "약관 및 정책 조회가 완료되었습니다"),
    CONTACT_CREATE(HttpStatus.OK,200, "문의 작성이 완료되었습니다"),
    CONTACT_GET(HttpStatus.OK,200, "문의 조회가 완료되었습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
