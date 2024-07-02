package org.meme.domain.Repository;

import org.meme.domain.Entity.FavoritePortfolio;
import org.meme.domain.Entity.Model;
import org.meme.domain.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePortfolioRepository extends JpaRepository<FavoritePortfolio, Long> {
    List<FavoritePortfolio> findByModel(Model model);
    boolean existsByModelAndPortfolio(Model model, Portfolio portfolio);
    Optional<FavoritePortfolio> findByModelAndPortfolio(Model model, Portfolio portfolio);
}
