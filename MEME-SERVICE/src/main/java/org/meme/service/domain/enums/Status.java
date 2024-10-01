package org.meme.service.domain.enums;

public enum Status {
    PENDING,    // 예약 대기
    APPROVED,   // 예약 승인
    CANCELED,   // 예약 취소
    COMPLETED,  // 진행 완료
    REVIEWED    // 리뷰 작성

    // 예약 진행 프로세스
    // PENDING -> APPROVED -> COMPLETED -> REVIEWED

    // 예약 취소 프로세스
    // PENDING -> CANCELED
}