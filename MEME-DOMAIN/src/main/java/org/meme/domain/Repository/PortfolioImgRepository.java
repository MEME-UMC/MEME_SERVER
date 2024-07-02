package org.meme.domain.Repository;

import org.meme.domain.Entity.Portfolio;
import org.meme.domain.Entity.PortfolioImg;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PortfolioImgRepository extends JpaRepository<PortfolioImg, Long> {
    Optional<PortfolioImg> findBySrcAndPortfolio(String src, Portfolio portfolio);
}
