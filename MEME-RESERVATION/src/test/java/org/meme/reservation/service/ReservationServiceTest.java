package org.meme.reservation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meme.domain.common.exception.ReservationException;
import org.meme.domain.entity.Model;
import org.meme.domain.entity.Portfolio;
import org.meme.domain.entity.Reservation;
import org.meme.domain.repository.ModelRepository;
import org.meme.domain.repository.PortfolioRepository;
import org.meme.domain.repository.ReservationRepository;
import org.meme.reservation.dto.ReservationRequest;
import org.meme.reservation.dto.ReservationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ModelRepository modelRepository;
    @MockBean
    private PortfolioRepository portfolioRepository;
    @MockBean
    private ReservationRepository reservationRepository;

    @Test
    void 예약_성공() {
        // Given
        ReservationRequest.SaveDto request = new ReservationRequest.SaveDto();
        request.setTimes(Set.of("18:30", "19:00", "19:30"));

        Model mockedModel = mock(Model.class);
        Portfolio mockedPortfolio = mock(Portfolio.class);

        when(modelRepository.findById(any())).thenReturn(Optional.of(mockedModel));
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(mockedPortfolio));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        // When
        when(reservationRepository.findByPortfolioAndYearAndMonthAndDay(any(Portfolio.class), anyInt(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());  // null 값 반환
        ReservationResponse.SuccessDto successDto = reservationService.makeReservation(request, 1L);

        // Then
        Assertions.assertNotNull(successDto);
    }

    @Test
    void 예약_실패() {
        // Given
        ReservationRequest.SaveDto request = new ReservationRequest.SaveDto();
        request.setTimes(Set.of("18:30", "19:00", "19:30"));

        Model mockedModel = mock(Model.class);
        Portfolio mockedPortfolio = mock(Portfolio.class);

        when(modelRepository.findById(any())).thenReturn(Optional.of(mockedModel));
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(mockedPortfolio));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        // When
        when(reservationRepository.findByPortfolioAndYearAndMonthAndDay(any(Portfolio.class), anyInt(), anyInt(), anyInt()))
                .thenReturn(Optional.of(getReservations()));  // 예약 내역 반환

        // Then
        Assertions.assertThrows(ReservationException.class,
                () -> reservationService.makeReservation(request, 1L));
    }

    private List<Reservation> getReservations() {
        Reservation reservation1 = Reservation.builder()
                .times("18:00,18:30,19:00")
                .build();
        Reservation reservation2 = Reservation.builder()
                .times("21:00,21:30,22:00")
                .build();

        return List.of(reservation1, reservation2);
    }
}