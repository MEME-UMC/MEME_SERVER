package org.meme.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.service.common.BaseEntity;
import org.meme.service.domain.entity.Portfolio;
import org.meme.service.domain.entity.User;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FavoritePortfolio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoritePortfolioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="portfolio_id", nullable = false)
    private Portfolio portfolio;

}
