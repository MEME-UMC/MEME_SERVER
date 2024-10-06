package org.meme.reservation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.meme.reservation.common.BaseEntity;

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

//    @ManyToOne
//    @JoinColumn(name="user_id", nullable = false)
//    private Artist artist;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String makeupName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private String durationTime; //소요시간

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "portfolio")
    private List<PortfolioImg> portfolioImgList;

    @Column
    private String averageStars;

    @Column(nullable = false, columnDefinition = "TINYINT(1) default 0")
    private boolean isBlock;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "portfolio")
//    private List<Review> reviewList;

    @OneToMany(mappedBy = "portfolio")
    private List<Reservation> reservations;

    public void updateReservationList(Reservation reservation){
        this.reservations.add(reservation);
    }

    public boolean isBlock(){
        return this.isBlock;
    }


//    public void updateReviewList(Review review){
//        this.reviewList.add(review);
//        //별점 업데이트
//        updateAverageStars();
//    }
//
//    private void updateAverageStars(){
//        int count = this.reviewList.size();
//        if(count == 0) {
//            this.averageStars = "0.00";
//            return;
//        }
//
//        double stars = 0;
//        for(Review review : reviewList){
//            stars += review.getStar();
//        }
//        this.averageStars = String.format("%.2f", stars/count);
//    }

    public void updatePortfolioImgList(List<PortfolioImg> portfolioImgList){this.portfolioImgList = portfolioImgList;}

//    public void addPortfolioImg(PortfolioImg portfolioImg) {
//        this.portfolioImgList.add(portfolioImg);
//        portfolioImg.setPortfolio(this);
//    }
}
