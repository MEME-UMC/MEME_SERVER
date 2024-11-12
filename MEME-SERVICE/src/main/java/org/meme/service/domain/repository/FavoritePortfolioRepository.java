package org.meme.service.domain.repository;

import org.meme.service.domain.entity.FavoritePortfolio;
import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoritePortfolioRepository extends JpaRepository<FavoritePortfolio, Long> {
    boolean existsByModelAndPortfolio(Model model, Portfolio portfolio);
    Optional<FavoritePortfolio> findByModelAndPortfolio(Model model, Portfolio portfolio);

    @Query("SELECT fp " +
            "FROM FavoritePortfolio fp " +
            "JOIN FETCH fp.portfolio p " +
            "JOIN FETCH p.portfolioImgList pil " +
            "WHERE fp.model = :model " +
            "ORDER BY fp.createdAt desc")
    Page<FavoritePortfolio> findFavoritePortfolioByModel(
            @Param(value = "model") Model model,
            Pageable pageable);
}
