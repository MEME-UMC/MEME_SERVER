package org.meme.service.domain.service;

import lombok.RequiredArgsConstructor;
import org.meme.service.common.exception.GeneralException;
import org.meme.service.domain.entity.*;
import org.meme.service.domain.enums.Status;
import org.meme.service.domain.repository.*;
import org.meme.service.domain.converter.ReviewConverter;
import org.meme.service.domain.dto.request.ReviewRequest;
import org.meme.service.domain.dto.response.ReviewResponse;
import org.meme.service.domain.repository.PortfolioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.meme.service.common.status.ErrorStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ModelRepository modelRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ReservationRepository reservationRepository;
    private final PortfolioRepository portfolioRepository;

    //리뷰 작성
    @Transactional
    public Long createReview(ReviewRequest.ReviewDto reviewDto) {
        Model model = findModelById(reviewDto.getModelId());
        Reservation reservation = findReservationByIdAndModel(reviewDto.getReservationId(), reviewDto.getModelId());

        validReservationStatus(reservation);

        // 리뷰 이미지 리스트 생성
        List<ReviewImg> reviewImgList = reviewDto.getReviewImgSrc().stream()
                .map(ReviewConverter::toReviewImg)
                .toList();

        // 리뷰 entity 생성
        Portfolio portfolio = reservation.getPortfolio();
        Review review = ReviewConverter.toReview(model, portfolio, reviewDto);

        // 리뷰 이미지, 리뷰 연관관계 설정
        reviewImgList.forEach(review::addReviewImg);

        // 리뷰 연관관게 설정
        portfolio.updateReviewList(review);
        model.updateReviewList(review);
        portfolio.updateReviewCount();

        reviewRepository.save(review);

        reservation.updateStatus(Status.REVIEWED);
        return review.getReviewId();
    }

    //리뷰 상세 조회
    public ReviewResponse.ReviewDetailsDto getReviewDetails(Long reviewId){
        Review review = findReviewById(reviewId);
        return ReviewConverter.toReviewDetailDto(review);
    }

    //내가 쓴 리뷰 조회
    public List<ReviewResponse.MyReviewDto> getMyReview(Long modelId){
        Model model = findModelById(modelId);

        //리뷰 리스트 조회
        List<Review> reviewList = reviewRepository.findByModel(model);
        return reviewList.stream()
                .map(ReviewConverter::toMyReviewDto)
                .toList();
    }

    //리뷰 리스트 조회
    public ReviewResponse.ReviewListPageDto getReviewList(Long portfolioId, int page) {
        Portfolio portfolio = findPortfolioById(portfolioId);

        Pageable pageable = PageRequest.of(page, 30);
        Page<Review> reviewPage = reviewRepository.findReviewsByPortfolio(portfolio, pageable);

        return ReviewConverter.toReviewListPageDto(reviewPage);
    }

    //리뷰 작성 가능 예약 리스트 조회
    public List<ReviewResponse.ReviewAvailableListDto> getReviewReservationList(Long modelId){
        Model model = findModelById(modelId);
        List<Reservation> reservationList = reservationRepository.findReservationByStatus(model);

        return reservationList.stream()
                .map(ReviewConverter::toReviewAvailableListDto)
                .toList();
    }

    //리뷰 수정
    @Transactional
    public ReviewResponse.ReviewDetailsDto updateReview(ReviewRequest.UpdateReviewDto updateReviewDto){
        Model model = findModelById(updateReviewDto.getModelId());
        Review review = findReviewById(updateReviewDto.getReviewId());
        validIsUserAuthorizedForReview(model, review);

        // 리뷰 이미지 수정
        if (!updateReviewDto.getReviewImgSrcList().isEmpty())
            updateReviewImgList(review, updateReviewDto.getReviewImgSrcList());

        // 리뷰 수정
        updateReview(review, updateReviewDto);
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
                ReviewImg newReviewImg = ReviewConverter.toReviewImg(reviewImgSrc);
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
                reviewImgRepository.delete(reviewImg);
            }
        }

        // 리뷰 이미지 리스트 - 리뷰 연관관계 설정
        review.updateReviewImgList(updatedReviewImgList);
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(ReviewRequest.DeleteReviewDto reviewDto){
        Model model = findModelById(reviewDto.getModelId());
        Review review = findReviewById(reviewDto.getReviewId());
        validIsUserAuthorizedForReview(model, review);

        review.getPortfolio().decreaseReviewCount();
        reviewRepository.delete(review);
    }

    private void validIsUserAuthorizedForReview(Model model, Review review) {
        if(!Objects.equals(review.getModel().getUserId(), model.getUserId()))
            throw new GeneralException(ErrorStatus.INVALID_MODEL_FOR_REVIEW);
    }

    private void validReservationStatus(Reservation reservation) {
        if (reservation.isReviewed())
            throw new GeneralException(ErrorStatus.ALREADY_REVIEWED);

        if (!reservation.isCompleted())
            throw new GeneralException(ErrorStatus.INVALID_REVIEW_REQUEST);
    }

    private Model findModelById(Long modelId){
        return modelRepository.findModelByUserId(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
    }

    private Portfolio findPortfolioById(Long portfolioId){
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO));
    }

    private Review findReviewById(Long reviewId){
        return reviewRepository.findReviewById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_REVIEW));
    }

    private Reservation findReservationByIdAndModel(Long reservationId, Long modelId){
        return reservationRepository.findByReservationIdAndModelId(reservationId, modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_RESERVATION));
    }

    private void updateReview(Review review, ReviewRequest.UpdateReviewDto updateReviewDto){
        if(updateReviewDto.getStar() > 0)
            review.setStar(updateReviewDto.getStar());
        if(updateReviewDto.getComment() != null)
            review.setComment(updateReviewDto.getComment());
    }
}
