package org.meme.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meme.service.common.BaseEntity;
import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.Portfolio;
import org.meme.service.domain.enums.Status;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private int price;

    @Version
    private int version;

    public void pendingReservation(Model model, Portfolio portfolio) {
        this.model = model;
        model.getReservationList().add(this);

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
