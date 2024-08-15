package org.meme.reservation.service;

import lombok.RequiredArgsConstructor;
import org.meme.domain.common.exception.ReservationException;
import org.meme.domain.common.status.ErrorStatus;
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
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.meme.domain.enums.Status.*;

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
    public ReservationResponse.SuccessDto makeReservation(ReservationRequest.SaveDto requestDto) {
        Model model = modelRepository.findById(requestDto.getModel_id())
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
        Portfolio portfolio = portfolioRepository.findById(requestDto.getPortfolio_id())
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        try {
            return concurrentRequestHandler.handleConcurrency(requestDto, model, portfolio)
                    .thenApply(ReservationConverter::toSuccessDto).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new ReservationException(ErrorStatus.RESERVATION_NOT_FOUND);
        }
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

    @Transactional
    public void updateEnableDate(ReservationRequest.EnableDateDto enableDateUpdateDto, Long artistId) {
        String enableDates = ReservationConverter.intoDateString(enableDateUpdateDto.getEnable_date());

        ArtistEnableDate enableDate = enableDateRepository.findByArtist_UserId(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        enableDate.updateEnableDates(enableDates);
    }

    @Transactional
    public void updateEnableTime(ReservationRequest.EnableTimeDto enableTimeUpdateDto, Long artistId) {
        String enableTimes = ReservationConverter.intoTimeString(enableTimeUpdateDto.getEnable_time());

        ArtistEnableTime enableTime = enableTimeRepository.findByArtist_UserId(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        enableTime.updateEnableTimes(enableTimes);
    }

    public List<ReservationResponse.ReservationSimpleDto> getReservationSimplesByArtist(Long artistId) {
        Artist artist = getArtistById(artistId);
        List<Portfolio> portfolioList = artist.getPortfolioList();

        List<ReservationResponse.ReservationSimpleDto> reservationSimpleDtos = new ArrayList<>();

        for (Portfolio portfolio : portfolioList) {
            List<Reservation> reservations = portfolio.getReservations();  // 포트폴리오의 예약 목록 조회
            for (Reservation reservation : reservations) {
                ReservationResponse.ReservationSimpleDto reservationSimpleDto = ReservationConverter.toReservationSimpleDto(reservation);
                reservationSimpleDtos.add(reservationSimpleDto);
            }
        }

        return reservationSimpleDtos;
    }

    public List<ReservationResponse.ReservationSimpleDto> getReservationSimplesByModel(Long modelId) {
        Model model = getModelById(modelId);
        List<Reservation> reservations = model.getReservations();

        List<ReservationResponse.ReservationSimpleDto> reservationSimpleDtos = new ArrayList<>();

        for (Reservation reservation : reservations) {
            ReservationResponse.ReservationSimpleDto reservationSimpleDto = ReservationConverter.toReservationSimpleDto(reservation);
            reservationSimpleDtos.add(reservationSimpleDto);
        }

        return reservationSimpleDtos;
    }

    public ReservationResponse.ReservationDetailArtistSightDto getReservationDetailByArtist(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        return ReservationConverter.toReservationDetailArtistSightDto(reservation);
    }

    public ReservationResponse.ReservationDetailModelSightDto getReservationDetailByModel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        return ReservationConverter.toReservationDetailModelSightDto(reservation);
    }

    @Transactional
    public void changeReservationStatusApproved(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getStatus() == PENDING) {
            reservation.updateStatus(APPROVED);
        }
    }

    @Transactional
    public void changeReservationStatusCanceled(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getStatus() == PENDING) {
            reservation.updateStatus(CANCELED);
            reservationRepository.delete(reservation);
        }
    }

    private Artist getArtistById(Long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
    }

    private Model getModelById(Long modelId) {
        return modelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));
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
