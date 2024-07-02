package org.meme.domain.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.DayOfWeek;
import org.meme.domain.enums.Times;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AvailableTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "available_time_id")
    private Long availableTimeId;

    @Column(nullable = false)
    private LocalDate date; //날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek; //요일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Times times; //시간

    @Column(nullable = false, columnDefinition = "TINYINT(1) default 0")
    private boolean isReservated; //해당 시간대 예약 여부

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Artist artist;

    @OneToOne(mappedBy = "availableTime", fetch = FetchType.LAZY)
    private Reservation reservation;

    public void updateIsReservated(boolean isReservated){
        this.isReservated = isReservated;
    }

    public void updateReservation(Reservation reservation){this.reservation = reservation;}

    public void updateArtist(Artist artist){this.artist = artist;}

    // DTO 넣지 말라고!

//    public static AvailableTime from(AvailableTimeRequestDto.AvailableTimeDto request){
//        return AvailableTime.builder()
//                .date(request.getDate())
//                .dayOfWeek(request.getDayOfWeek())
//                .times(request.getTimes())
//                .build();
//    }

}
