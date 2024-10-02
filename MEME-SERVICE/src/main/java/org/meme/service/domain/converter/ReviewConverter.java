package org.meme.service.domain.converter;

import org.meme.service.domain.entity.Reservation;
import org.meme.service.domain.entity.*;
import org.meme.service.domain.dto.request.ReviewRequest;
import org.meme.service.domain.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewConverter {
    public static Review toReview(Model model, Portfolio portfolio, ReviewRequest.ReviewDto dto){
        return Review.builder()
                .model(model)
                .portfolio(portfolio)
                .star(dto.getStar())
                .comment(dto.getComment())
                .reviewImgList(new ArrayList<ReviewImg>())
                .build();
    }

    public static ReviewResponse.ReviewDto toReviewDto(Review review){
        List<ReviewResponse.ReviewImgDto> reviewImgDtoList = review.getReviewImgList()
                .stream()
                .map(ReviewConverter::toReviewImgDto)
                .toList();

        return ReviewResponse.ReviewDto.builder()
                .reviewId(review.getReviewId())
                .modelNickName(review.getModel().getNickname())
                .modelProfileImg(review.getModel().getProfileImg())
                .star(review.getStar())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .reviewImgDtoList(reviewImgDtoList)
                .build();
    }

    public static ReviewResponse.ReviewDetailsDto toReviewDetailDto(Review review){
        List<ReviewResponse.ReviewImgDto> reviewImgDtoList = review.getReviewImgList()
                .stream()
                .map(ReviewConverter::toReviewImgDto)
                .toList();

        return ReviewResponse.ReviewDetailsDto.builder()
                .artistNickName(review.getPortfolio().getArtist().getNickname())
                .makeupName(review.getPortfolio().getMakeupName())
                .star(review.getStar())
                .comment(review.getComment())
                .reviewImgDtoList(reviewImgDtoList)
                .build();
    }

    public static ReviewResponse.MyReviewDto toMyReviewDto(Review review){
        return ReviewResponse.MyReviewDto.builder()
                .reviewId(review.getReviewId())
                .artistNickName(review.getPortfolio().getArtist().getNickname())
                .makeupName(review.getPortfolio().getMakeupName())
                .portfolioImg(review.getPortfolio().getPortfolioImgList().get(0).getSrc()) //첫 번째 포트폴리오 이미지
                .location(review.getPortfolio().getArtist().getShopLocation())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static ReviewResponse.ReviewAvailableListDto toReviewAvailableListDto(Reservation reservation){
        return ReviewResponse.ReviewAvailableListDto.builder()
                .reservationId(reservation.getReservationId())
                .portfolioId(reservation.getReservationId())
                .artistNickName(reservation.getPortfolio().getArtist().getNickname())
                .makeupName(reservation.getPortfolio().getMakeupName())
                .portfolioImg(reservation.getPortfolio().getPortfolioImgList().get(0).getSrc())
                .reservationYear(reservation.getYear())
                .reservatioinMonth(reservation.getMonth())
                .reservationiDay(reservation.getDay())
                .reservationTimes(reservation.getTimes())
                .shopLocation(reservation.getLocation())
                .build();
    }

    public static ReviewImg toReviewImg(String src){
        return ReviewImg.builder()
                .src(src)
                .build();
    }

    public static ReviewResponse.ReviewImgDto toReviewImgDto(ReviewImg img){
        return ReviewResponse.ReviewImgDto.builder()
                .reviewImgId(img.getReviewImgId())
                .reviewImgSrc(img.getSrc())
                .build();
    }

    public static ReviewResponse.ReviewListPageDto toReviewListPageDto(Page<Review> page){
        List<ReviewResponse.ReviewDto> content = page.stream()
                .map(ReviewConverter::toReviewDto)
                .toList();

        return ReviewResponse.ReviewListPageDto.builder()
                .content(content)
                .starStatus(setStarStatus(page.getContent())) //별점 현황
                .pageSize(page.getSize())
                .currentPage(page.getNumber())
                .totalNumber(page.getNumberOfElements())
                .totalPage(page.getTotalPages())
                .build();
    }

    private static Map<Integer, Integer> setStarStatus(List<Review> list){

        Map<Integer, Integer> starStatus = new HashMap<>(Map.of(5, 0, 4, 0, 3, 0, 2, 0, 1, 0));
        for (Review review : list) {
            int star = review.getStar();
            starStatus.put(star, starStatus.get(star) + 1);
        }
        return starStatus;
    }


}
