package org.meme.domain.Repository;

import org.meme.domain.Entity.Model;
import org.meme.domain.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByModel(Model model);
}
