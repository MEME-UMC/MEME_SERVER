package org.meme.service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.meme.service.common.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter @Setter
public class ReviewImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id", nullable = false)
    private Review review;

    @Column(nullable = false)
    private String src;

    public void updateSrc(String src){this.src = src;}
}
