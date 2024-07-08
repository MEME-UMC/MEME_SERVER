package org.meme.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.exception.GeneralException;
import org.meme.domain.entity.*;
import org.meme.domain.enums.Status;
import org.meme.domain.repository.*;
import org.meme.service.converter.ReviewConverter;
import org.meme.service.dto.ReviewRequest;
import org.meme.service.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.meme.domain.common.status.ErrorStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ModelRepository modelRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ReservationRepository reservationRepository;
    private final PortfolioRepository portfolioRepository;

    //리뷰 작성
    @Transactional
    public Long createReview(ReviewRequest.ReviewDto reviewDto) {
        Model model = modelRepository.findById(reviewDto.getModelId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
        Reservation reservation = reservationRepository.findByReservationIdAndModelId(reviewDto.getReservationId(), reviewDto.getModelId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_RESERVATION));

        // 이미 리뷰 작성 완료
        if (reservation.isReview())
            throw new GeneralException(ErrorStatus.ALREADY_REVIEWED);

        // 예약 미완료
        if (reservation.getStatus() != Status.COMPLETE)
            throw new GeneralException(ErrorStatus.INVALID_REVIEW_REQUEST);

        // 리뷰 이미지 리스트 생성
        List<ReviewImg> reviewImgList = reviewDto.getReviewImgSrc().stream()
                .map(ReviewImg::from)
                .toList();

        // 리뷰 entity 생성
        Portfolio portfolio = reservation.getPortfolio();
        Review review = ReviewConverter.toReview(model, portfolio, reviewDto);

        // 리뷰 이미지, 리뷰 연관관계 설정
        reviewImgList.forEach(review::addReviewImg);

        // 리뷰 연관관게 설정
        portfolio.updateReviewList(review);
        model.updateReviewList(review);

        reviewRepository.save(review);
        reservation.updateIsReview(true);
        return review.getReviewId();
    }

    //리뷰 상세 조회
    public ReviewResponse.ReviewDetailsDto getReviewDetails(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_REVIEW));
        return ReviewConverter.toReviewDetailDto(review);
    }

    //내가 쓴 리뷰 조회
    public List<ReviewResponse.MyReviewDto> getMyReview(Long modelId){
        Model model = modelRepository.findById(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));

        //리뷰 리스트 조회
        List<Review> reviewList = reviewRepository.findByModel(model);
        return reviewList.stream()
                .map(ReviewConverter::toMyReviewDto)
                .toList();
    }

    //리뷰 리스트 조회
    public ReviewResponse.ReviewListPageDto getReviewList(Long portfolioId, int page) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO));

        // list를 page로 변환
        List<Review> reviewList = portfolio.getReviewList();
        Page<Review> reviewPage = getPage(page, reviewList);

        return ReviewConverter.toReviewListPageDto(reviewPage);
    }

    //리뷰 작성 가능 예약 리스트 조회
    public List<ReviewResponse.ReviewAvailableListDto> getReviewReservationList(Long modelId){
        Model model = modelRepository.findById(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
        List<Reservation> reservationList = model.getReservationList();

        //status != COMPLETE 이면 리스트에서 제거
        reservationList.removeIf(Reservation::isAvailableReview);

        //리뷰 작성 완료시 리스트에서 제거
        reservationList.removeIf(Reservation::isReview);

        return reservationList.stream()
                .map(ReviewConverter::toReviewAvailableListDto)
                .toList();
    }

    //리뷰 수정
    @Transactional
    public ReviewResponse.ReviewDetailsDto updateReview(ReviewRequest.UpdateReviewDto updateReviewDto){
        Model model = modelRepository.findById(updateReviewDto.getModelId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
        Review review = reviewRepository.findById(updateReviewDto.getReviewId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_REVIEW));

        if (!review.getModel().equals(model))
            throw new GeneralException(ErrorStatus.INVALID_MODEL_FOR_REVIEW);

        // 리뷰 이미지 수정
        if (!updateReviewDto.getReviewImgSrcList().isEmpty())
            updateReviewImgList(review, updateReviewDto.getReviewImgSrcList());

        // 리뷰 수정
        // TODO:
//        review.updateReview(updateReviewDto);
        return ReviewConverter.toReviewDetailDto(review);
    }

    @Transactional
    public void updateReviewImgList(Review review, List<String> reviewImgSrcList){
        List<ReviewImg> updatedReviewImgList = new ArrayList<>();

        for(String reviewImgSrc : reviewImgSrcList) {
            if (reviewImgSrc == null)
                throw new GeneralException(ErrorStatus.NOT_EXIST_REVIEW_IMG);

            Optional<ReviewImg> reviewImg = reviewImgRepository.findBySrcAndReview(reviewImgSrc, review);
            if (reviewImg.isEmpty()) {
                // 새로운 이미지 추가
                ReviewImg newReviewImg = ReviewImg.from(reviewImgSrc);
                newReviewImg.setReview(review);
                reviewImgRepository.save(newReviewImg);
                updatedReviewImgList.add(newReviewImg);
            } else {
                // 기존 이미지 보존
                updatedReviewImgList.add(reviewImg.get());
            }
        }

        // 기존 리뷰 이미지 리스트와 새로운 리뷰 이미지 리스트 비교
        List<ReviewImg> existedReviewImgList = review.getReviewImgList();
        for (ReviewImg reviewImg : existedReviewImgList){
            if (!updatedReviewImgList.contains(reviewImg)){
                // 이미지 삭제
                reviewImgRepository.delete(reviewImg);
            }
        }

        // 리뷰 이미지 리스트 - 리뷰 연관관계 설정
        review.updateReviewImgList(updatedReviewImgList);
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(ReviewRequest.DeleteReviewDto reviewDto){
        Model model = modelRepository.findById(reviewDto.getModelId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
        Review review = reviewRepository.findById(reviewDto.getReviewId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_REVIEW));
        if(!review.getModel().equals(model))
            throw new GeneralException(ErrorStatus.INVALID_MODEL_FOR_REVIEW);

        reviewRepository.delete(review);
    }

    private Page<Review> getPage(int page, List<Review> list){
        Pageable pageable = PageRequest.of(page, 30);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        //list를 page로 변환
        return new PageImpl<>(list.subList(start, end),
                pageable, list.size());
    }
}
