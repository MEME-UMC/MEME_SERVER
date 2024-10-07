package org.meme.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByModelAndPortfolio(Model model, Portfolio portfolio);

    // 예약 겹치는 여부 체크
    Optional<List<Reservation>> findByPortfolioAndYearAndMonthAndDay(Portfolio portfolio, int year, int month, int day);

    // 연도, 월에 맞춰 예약 내역 불러오기
    Optional<List<Reservation>> findByPortfolioAndYearAndMonth(Portfolio portfolio, int year, int month);

    Optional<List<Reservation>> findByModel_UserId(Long userId);
}


