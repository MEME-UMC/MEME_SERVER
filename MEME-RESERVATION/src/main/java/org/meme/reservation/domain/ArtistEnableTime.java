package org.meme.reservation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ArtistEnableTime {

    @Id
    private Long userId;

    @OneToOne
    @MapsId  // userId를 외래키로 사용하고, 동시에 Primary Key로 매핑
    @JoinColumn(name = "user_id")
    private org.meme.reservation.domain.Artist artist;

    @Column(length = 3000)  // TODO : 최대 길이 파악하고 수정하기
    private String enableTimes;

    @Builder
    public ArtistEnableTime(Artist artist, String enableTimes) {
        this.artist = artist;
        this.enableTimes = enableTimes;
    }

    public void updateEnableTimes(String enableTimes) {
        this.enableTimes = enableTimes;
    }
}
