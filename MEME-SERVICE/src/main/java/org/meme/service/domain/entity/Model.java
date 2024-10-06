package org.meme.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.meme.service.domain.entity.*;
import org.meme.service.domain.enums.Gender;
import org.meme.service.domain.enums.PersonalColor;
import org.meme.service.domain.enums.SkinType;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Model {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

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

    public void updateFavoriteArtistList(FavoriteArtist artist){
        this.favoriteArtistList.add(artist);
    }

    public void updateFavoritePortfolioList(FavoritePortfolio portfolio){
        this.favoritePortfolioList.add(portfolio);
    }

    public void updateReviewList(Review review){
        this.reviewList.add(review);
    }

    public void updateProfileImg(String profileimg){
        this.user.updateProfileImg(profileimg);
    }

    public void updateNickname(String nickname){
        this.user.updateNickname(nickname);
    }

    public void updateGender(Gender gender){
        this.user.updateGender(gender);
    }

}
