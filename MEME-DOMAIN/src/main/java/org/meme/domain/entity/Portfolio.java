package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.meme.domain.common.BaseEntity;
import org.meme.domain.enums.Category;


import java.util.List;


@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Portfolio extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long portfolioId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Artist artist;
}
