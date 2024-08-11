package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.meme.domain.enums.PersonalColor;
import org.meme.domain.enums.SkinType;

import java.util.List;

@SuperBuilder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Model extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkinType skinType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonalColor personalColor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "model")
    private List<FavoritePortfolio> favoritePortfolioList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "model")
    private List<FavoriteArtist> favoriteArtistList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "model")
    private List<Reservation> reservationList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "model")
    private List<Review> reviewList;

    // daeun) OK 시 이거로 적용하기
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "model")
    private List<Reservation> reservations;

    public void updateFavoriteArtistList(FavoriteArtist artist){
        this.favoriteArtistList.add(artist);
    }

    public void updateFavoritePortfolioList(FavoritePortfolio portfolio){
        this.favoritePortfolioList.add(portfolio);
    }
    public void updateReservationList(Reservation reservation){
        this.reservationList.add(reservation);
    }

    public void updateReviewList(Review review){
        this.reviewList.add(review);
    }


    //temp create model builder
//    public static Model from(ModelProfileDto dto){
//        return Model.builder()
//                .profileImg(dto.getProfileImg())
//                .nickname(dto.getNickname())
//                .gender(dto.getGender())
//                .skinType(dto.getSkinType())
//                .personalColor(dto.getPersonalColor())
//                .build();
//    }

//    public void tempMethod(){
//        this.username = "name";
//        this.email="";
//        this.password="";
//        this.role="MODEL";
//        this.userStatus = UserStatus.ACTIVE;
//        this.provider = Provider.KAKAO;
//    }
}
