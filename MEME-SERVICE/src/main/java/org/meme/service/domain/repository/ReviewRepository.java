package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByModel(Model model);
}
