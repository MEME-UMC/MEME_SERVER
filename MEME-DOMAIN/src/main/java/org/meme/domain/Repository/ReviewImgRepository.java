package org.meme.domain.Repository;

import org.meme.domain.Entity.Review;
import org.meme.domain.Entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    Optional<ReviewImg> findBySrcAndReview(String src, Review review);
}
