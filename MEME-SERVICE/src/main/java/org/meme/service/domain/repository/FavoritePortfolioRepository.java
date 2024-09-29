package org.meme.service.domain.repository;

import org.meme.service.domain.entity.FavoritePortfolio;
import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritePortfolioRepository extends JpaRepository<FavoritePortfolio, Long> {
    List<FavoritePortfolio> findByModel(Model model);
    boolean existsByModelAndPortfolio(Model model, Portfolio portfolio);
    Optional<FavoritePortfolio> findByModelAndPortfolio(Model model, Portfolio portfolio);
}
