package org.meme.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReviewResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDto {
        private Long reviewId;
        private String modelNickName;
        private String modelProfileImg;
        private int star;
        private String comment;
        private LocalDateTime createdAt; //리뷰 작성 날짜
        private List<ReviewImgDto> reviewImgDtoList;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDetailsDto {
        private String artistNickName;
        private String makeupName;
        private int star;
        private String comment;
        private List<ReviewImgDto> reviewImgDtoList;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyReviewDto {
        private Long reviewId;
        private String artistNickName;
        private String makeupName;
        private String portfolioImg;
        private String location; //장소
        private LocalDateTime createdAt;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewAvailableListDto {
        private Long reservationId;
        private Long portfolioId;
        private String artistNickName;
        private String makeupName;
        private String portfolioImg;
        private int reservationYear;
        private int reservatioinMonth;
        private int reservationiDay;
        private String reservationTimes;
        private String shopLocation; //샵 위치
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewImgDto {
        private Long reviewImgId;
        private String reviewImgSrc;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListPageDto {
        private List<ReviewDto> content;
        private Map<Integer, Integer> starStatus;
        private int currentPage; //현재 페이지 번호
        private int pageSize; //페이지 크기
        private int totalNumber; //전체 메이크업 개수
        private int totalPage; //전체 페이지 개수
    }
}
