package org.meme.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.BaseResponseDto;
import org.meme.domain.common.status.SuccessStatus;
import org.meme.service.dto.ReviewRequest;
import org.springframework.web.bind.annotation.*;
import org.meme.service.service.ReviewService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 리스트 조회", description = "리뷰 리스트를 조회하는 API입니다.")
    @GetMapping("/{portfolioId}")
    public BaseResponseDto getReviewList(@PathVariable(name = "portfolioId") Long portfolioId,
                                         @RequestParam(value = "page", defaultValue = "0", required = false) int page
    ) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.REVIEW_GET, reviewService.getReviewList(portfolioId, page));
    }

    @Operation(summary = "리뷰 작성 가능 예약 리스트 조회", description = "리뷰 작성이 가능한 예약 리스트를 조회하는 API입니다.")
    @GetMapping("/available/{modelId}")
    public BaseResponseDto getReviewReservationList(@PathVariable(name = "modelId") Long modelId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.REVIEW_AVAILABLE_GET, reviewService.getReviewReservationList(modelId));
    }

    @Operation(summary = "리뷰 작성", description = "리뷰를 작성하는 API입니다.")
    @PostMapping()
    public BaseResponseDto createReview(@RequestBody ReviewRequest.ReviewDto reviewDto) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.REVIEW_CREATE, reviewService.createReview(reviewDto));
    }

    @Operation(summary = "리뷰 세부 조회", description = "리뷰를 세부 조회하는 API입니다.")
    @GetMapping("/details/{reviewId}")
    public BaseResponseDto getReviewDetails(@PathVariable(name = "reviewId") Long reviewId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.RESERVATION_DETAILS_GET, reviewService.getReviewDetails(reviewId));
    }

    @Operation(summary = "내가 쓴 리뷰 조회", description = "본인이 쓴 리뷰를 조회하는 API입니다.")
    @GetMapping("/me/{modelId}")
    public BaseResponseDto getMyReview(@PathVariable(name = "modelId") Long modelId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.REVIEW_GET, reviewService.getMyReview(modelId));
    }

    @Operation(summary = "리뷰 수정", description = "모델이 작성한 리뷰를 수정하는 API입니다.")
    @PatchMapping()
    public BaseResponseDto updateReview(@RequestBody ReviewRequest.UpdateReviewDto updateReviewDto) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.REVIEW_PATCH, reviewService.updateReview(updateReviewDto));
    }

    @Operation(summary = "리뷰 삭제", description = "모델이 작성한 리뷰를 삭제하는 API입니다.")
    @DeleteMapping()
    public BaseResponseDto deleteReview(@RequestBody ReviewRequest.DeleteReviewDto reviewDto) {
        reviewService.deleteReview(reviewDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.REVIEW_DELETE);
    }
}
