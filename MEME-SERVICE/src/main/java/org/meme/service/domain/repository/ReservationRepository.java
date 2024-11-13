package org.meme.service.domain.repository;

import org.meme.service.domain.entity.Artist;
import org.meme.service.domain.entity.Model;
import org.meme.service.domain.entity.Portfolio;
import org.meme.service.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN r.model m WHERE r.reservationId = :reservationId AND m.userId = :modelId")
    Optional<Reservation> findByReservationIdAndModelId(@Param("reservationId") Long reservationId, @Param("modelId") Long modelId);

    List<Reservation> findByModelAndPortfolio(Model model, Portfolio portfolio);

    // 예약 겹치는 여부 체크
    Optional<List<Reservation>> findByPortfolioAndYearAndMonthAndDay(Portfolio portfolio, int year, int month, int day);

    // 연도, 월에 맞춰 예약 내역 불러오기
    Optional<List<Reservation>> findByPortfolioAndYearAndMonth(Portfolio portfolio, int year, int month);
}


