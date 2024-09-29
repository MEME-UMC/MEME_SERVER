package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Review;
import org.meme.service.domain.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    Optional<ReviewImg> findBySrcAndReview(String src, Review review);
}
