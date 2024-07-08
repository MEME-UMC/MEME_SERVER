package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.domain.common.BaseEntity;
import org.meme.domain.enums.Category;


import java.util.List;


@Builder
@Getter
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String makeupName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String info;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "portfolio")
    private List<PortfolioImg> portfolioImgList;

    @Column
    private String averageStars;

    @Column(nullable = false, columnDefinition = "TINYINT(1) default 0")
    private boolean isBlock;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "portfolio")
    private List<Review> reviewList;

// DTO가 Entity 부분에 존재하면 객체지향적이지 못하므로 다른 곳에서 작성하도록!
//    public void updatePortfolio(UpdatePortfolioDto request) {
//
//        if(request.getCategory() != null){
//            this.category = request.getCategory();
//        }
//
//        if(request.getPrice() >= 0){
//            this.price = request.getPrice();
//        }
//
//        if(request.getInfo() != null){
//            this.info = request.getInfo();
//        }
//
//        if(request.getMakeupName() != null){
//            this.makeupName = request.getMakeupName();
//        }
//
//        this.isBlock = request.getIsBlock();
//    }

    public void updateReviewList(Review review){
        this.reviewList.add(review);
        //별점 업데이트
        updateAverageStars();
    }

    private void updateAverageStars(){
        int count = this.reviewList.size();
        if(count == 0) {
            this.averageStars = "0.00";
            return;
        }

        double stars = 0;
        for(Review review : reviewList){
            stars += review.getStar();
        }
        this.averageStars = String.format("%.2f", stars/count);
    }

    public void updatePortfolioImgList(List<PortfolioImg> portfolioImgList){this.portfolioImgList = portfolioImgList;}

    public void addPortfolioImg(PortfolioImg portfolioImg) {
        this.portfolioImgList.add(portfolioImg);
        portfolioImg.setPortfolio(this);
    }
}
