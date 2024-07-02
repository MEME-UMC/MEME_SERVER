package org.meme.domain.repository;

import org.meme.domain.entity.Portfolio;
import org.meme.domain.entity.PortfolioImg;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PortfolioImgRepository extends JpaRepository<PortfolioImg, Long> {
    Optional<PortfolioImg> findBySrcAndPortfolio(String src, Portfolio portfolio);
}
