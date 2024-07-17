package org.meme.reservation.service;

import lombok.RequiredArgsConstructor;
import org.meme.domain.common.exception.ReservationException;
import org.meme.domain.entity.*;
import org.meme.domain.enums.DayOfWeek;
import org.meme.domain.repository.*;
import org.meme.reservation.converter.ReservationConverter;
import org.meme.reservation.dto.ReservationRequest;
import org.meme.reservation.dto.ReservationResponse;
import org.meme.reservation.handler.ConcurrentRequestHandler;
import org.meme.reservation.handler.ScheduleHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ModelRepository modelRepository;
    private final PortfolioRepository portfolioRepository;
    private final ArtistRepository artistRepository;
    private final ArtistEnableDateRepository enableDateRepository;
    private final ArtistEnableTimeRepository enableTimeRepository;

    private final ConcurrentRequestHandler concurrentRequestHandler;
    private final ScheduleHandler scheduleHandler;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void makeReservation(ReservationRequest.SaveDto requestDto, Long portfolioId) {
        Model model = modelRepository.findById(requestDto.getModel_id())
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));

        concurrentRequestHandler.handleConcurrency(requestDto, model, portfolio);
    }

    public ReservationResponse.ScheduleYearAndMonthDto getScheduleByYearAndMonth(Long portfolioId, int year, int month) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        Long artistId = portfolio.getArtist().getUserId();

        ArtistEnableDate enableDate = enableDateRepository.findByArtist_UserId(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        ArtistEnableTime enableTime = enableTimeRepository.findByArtist_UserId(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        // 영업 가능 날짜 데이터 불러오기
        List<LocalDate> businessDaysList = ReservationConverter.intoDates(enableDate.getEnableDates());
        Set<LocalDate> businessDay = Set.copyOf(businessDaysList);

        // 영업 가능 시간 데이터 불러오기
        Map<DayOfWeek, List<String>> workTime = ReservationConverter.intoTimes(enableTime.getEnableTimes());

        // 예약 데이터 불러오기 (연도, 월)
        List<Reservation> reservationInYearAndMonth = reservationRepository.findByPortfolioAndYearAndMonth(portfolio, year, month)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        Map<Integer, List<String>> reservations = getReservations(reservationInYearAndMonth);

        return scheduleHandler.getSchedule(businessDay, workTime, reservations, year, month);
    }

    @Transactional
    public void addEnableDate(ReservationRequest.EnableDateDto enableDateDto, Long artistId) {
        Artist artist = getArtistById(artistId);
        ArtistEnableDate enableDate = ReservationConverter.toEnableDateEntity(enableDateDto, artist);
        enableDateRepository.save(enableDate);
    }

    @Transactional
    public void addEnableTime(ReservationRequest.EnableTimeDto enableTimeDto, Long artistId) {
        Artist artist = getArtistById(artistId);
        ArtistEnableTime enableTime = ReservationConverter.toEnableTimeEntity(enableTimeDto, artist);
        enableTimeRepository.save(enableTime);
    }

    public ReservationResponse.DateDto getEnableDate(Long artistId) {
        ArtistEnableDate artistEnableDate = enableDateRepository.findByArtist_UserId(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        return ReservationConverter.toDateDto(artistEnableDate);
    }

    public ReservationResponse.TimeDto getEnableTime(Long artistId) {
        ArtistEnableTime artistEnableTime = enableTimeRepository.findByArtist_UserId(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        return ReservationConverter.toTimeDto(artistEnableTime);
    }

    private Artist getArtistById(Long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
    }

    private Map<Integer, List<String>> getReservations(List<Reservation> reservationInYearAndMonth) {
        Map<Integer, List<String>> reservations = new HashMap<>();

        for (Reservation reservation : reservationInYearAndMonth) {
            int day = reservation.getDay();
            String[] split = reservation.getTimes().split(",");

            if (!reservations.containsKey(day)) {  // 키가 없으면
                reservations.put(day, List.of(split));
            } else {                               // 이미 키가 있으면
                List<String> times = reservations.get(day);
                times.addAll(List.of(split));
                reservations.replace(day, times);
            }
        }

        return reservations;
    }
}
