package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Review;
import org.meme.service.domain.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    Optional<ReviewImg> findBySrcAndReview(String src, Review review);
}
