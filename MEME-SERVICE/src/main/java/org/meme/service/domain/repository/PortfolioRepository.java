package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Artist;
import org.meme.service.domain.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query("SELECT p " +
            "FROM Portfolio p " +
            "JOIN FETCH p.portfolioImgList pl " +
            "WHERE p.artist = :artist " +
            "AND p.isBlock = false " +
            "ORDER BY p.createdAt desc")
    Page<Portfolio> findPortfoliosByArtist(
            @Param(value = "artist") Artist artist,
            Pageable pageable);

    boolean existsByMakeupName(String makeupName);


    @Query("SELECT p " +
            "FROM Portfolio p " +
            "JOIN FETCH p.portfolioImgList pl " +
            "JOIN FETCH p.artist a " +
            "JOIN FETCH a.user u " +
            "WHERE p.portfolioId = :portfolioId " +
            "AND p.isBlock = false ")
    Optional<Portfolio> findPortfolioById(@Param(value = "portfolioId") Long portfolioId);


    @Query("SELECT p " +
            "FROM Portfolio p " +
            "JOIN FETCH p.portfolioImgList pl " +
            "JOIN FETCH p.artist a " +
            "JOIN FETCH a.user u " +
            "WHERE p.isBlock = false " +
            "ORDER BY p.averageStars desc")
    List<Portfolio> findPortfolioByReviewList();

    @Query("SELECT p " +
            "FROM Portfolio p " +
            "JOIN FETCH p.portfolioImgList pl " +
            "JOIN FETCH p.artist a " +
            "JOIN FETCH a.user u " +
            "WHERE p.isBlock = false " +
            "ORDER BY p.createdAt desc")
    List<Portfolio> findPortfolioByCreatedAt();
}
