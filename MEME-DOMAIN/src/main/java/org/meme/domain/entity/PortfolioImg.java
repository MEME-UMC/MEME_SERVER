package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.meme.domain.common.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter @Setter
public class PortfolioImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolioImgId;

    @ManyToOne
    @JoinColumn(name="portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String src;

    public void updateSrc(String src) {
        this.src = src;
    }
}
