package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.Portfolio;
import org.meme.service.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN FETCH r.reviewImgList ril " +
            "JOIN FETCH r.portfolio p " +
            "JOIN FETCH p.artist a " +
            "JOIN FETCH a.user u " +
            "WHERE r.model = :model " +
            "ORDER BY r.createdAt desc ")
    List<Review> findByModel(@Param(value = "model") Model model);

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN FETCH r.reviewImgList ril " +
            "WHERE r.portfolio = :portfolio " +
            "ORDER BY r.createdAt desc ")
    Page<Review> findReviewsByPortfolio(
            @Param(value = "portfolio") Portfolio portfolio,
            Pageable pageable);

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN FETCH r.reviewImgList ril " +
            "JOIN FETCH r.portfolio p " +
            "JOIN FETCH p.artist a " +
            "JOIN FETCH a.user u " +
            "WHERE r.reviewId = :reviewId " +
            "ORDER BY r.createdAt desc ")
    Optional<Review> findReviewById(@Param(value = "reviewId") Long reviewId);
}
