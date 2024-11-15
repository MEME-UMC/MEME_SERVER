package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.domain.common.BaseEntity;
import org.meme.domain.enums.Status;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Model model;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private String times;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String location;

    private String makeupName;
    private String artistName;
    private int price;  // 실제 가격과 예약 시 할인 가격이 적용될 수도 있지 않을까

    @Version
    private int version;  // Optimistic Lock

    public void pendingReservation(Model model, Portfolio portfolio) {
        this.model = model;
        model.getReservations().add(this);

        this.portfolio = portfolio;
        portfolio.getReservations().add(this);
    }
    public boolean isReviewed(){
        return this.status == Status.REVIEWED;
    }

    public boolean isCompleted(){
        return this.status == Status.COMPLETED;
    }

    public void updateStatus(Status status){
        this.status = status;
    }

}
