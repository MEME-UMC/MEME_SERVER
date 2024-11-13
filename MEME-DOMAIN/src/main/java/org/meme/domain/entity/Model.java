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
    @OneToMany(mappedBy = "model")
    private List<Reservation> reservations;

}
