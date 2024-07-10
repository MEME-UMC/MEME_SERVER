package org.meme.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReviewRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDto {
        @NotBlank(message = "modelId를 입력해주세요")
        private Long modelId;
        @NotBlank(message = "reservationId를 입력해주세요")
        private Long reservationId;
        @NotBlank(message = "별점을 입력해주세요")
        private int star;
        private String comment;
        private List<String> reviewImgSrc;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReviewDto {
        @NotBlank(message = "modelId를 입력해주세요")
        private Long modelId;
        @NotBlank(message = "reviewId를 입력해주세요")
        private Long reviewId;
        private int star;
        private String comment;
        private List<String> reviewImgSrcList;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteReviewDto {
        @NotBlank(message = "modelId를 입력해주세요")
        private Long modelId;
        @NotBlank(message = "reviewId를 입력해주세요")
        private Long reviewId;
    }
}
