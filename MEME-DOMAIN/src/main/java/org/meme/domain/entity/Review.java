package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.meme.domain.common.BaseEntity;

import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name="portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User model;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "review")
    private List<ReviewImg> reviewImgList;

    @Column(nullable = false)
    private int star;

    @Column(nullable = true, length = 200)
    private String comment;

    public void updateReviewImgList(List<ReviewImg> reviewImgList){this.reviewImgList = reviewImgList;}

    public void addReviewImg(ReviewImg reviewImg) {
        this.reviewImgList.add(reviewImg);
        reviewImg.setReview(this);
    }
}
