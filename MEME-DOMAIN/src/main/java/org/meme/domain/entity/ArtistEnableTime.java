package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ArtistEnableTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Artist artist;

    @Column(length = 3000)  // TODO : 최대 길이 파악하고 수정하기
    private String enableTimes;

    @Builder
    public ArtistEnableTime(Artist artist, String enableTimes) {
        this.artist = artist;
        this.enableTimes = enableTimes;
    }
}
