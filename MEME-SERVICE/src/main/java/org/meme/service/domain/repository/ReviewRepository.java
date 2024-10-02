package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByModel(Model model);
}
