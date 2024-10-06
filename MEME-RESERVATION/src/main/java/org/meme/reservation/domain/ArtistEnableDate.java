package org.meme.reservation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ArtistEnableDate {

    @Id
    private Long userId;

    @OneToOne
    @MapsId  // userId를 외래키로 사용하고, 동시에 Primary Key로 매핑
    @JoinColumn(name = "user_id")
    private Artist artist;

    private int year;

    private int month;

    private String enableDates;

    @Builder
    public ArtistEnableDate(Artist artist, int year, int month, String enableDates) {
        this.artist = artist;
        this.year = year;
        this.month = month;
        this.enableDates = enableDates;
    }

    public void updateEnableDates(String enableDates) {
        this.enableDates = enableDates;
    }
}
