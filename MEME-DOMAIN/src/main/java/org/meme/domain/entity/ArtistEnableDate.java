package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ArtistEnableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
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
}
