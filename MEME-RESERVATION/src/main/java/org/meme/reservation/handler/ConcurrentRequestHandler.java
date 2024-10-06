package org.meme.reservation.handler;

import lombok.RequiredArgsConstructor;
import org.meme.reservation.common.exception.ReservationConflictException;
import org.meme.reservation.common.exception.ReservationException;
import org.meme.reservation.converter.ReservationConverter;
import org.meme.reservation.domain.Model;
import org.meme.reservation.domain.Portfolio;
import org.meme.reservation.domain.Reservation;
import org.meme.reservation.domain.ReservationRepository;
import org.meme.reservation.dto.ReservationRequest;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;

import static org.meme.reservation.common.status.ErrorStatus.RESERVATION_CANNOT_ACQUIRE_LOCK;
import static org.meme.reservation.common.status.ErrorStatus.RESERVATION_CONFLICT;

@RequiredArgsConstructor
@Service
public class ConcurrentRequestHandler {

    private final ReservationRepository reservationRepository;
    private final RedissonClient redissonClient;

    @Async
    @Transactional
    public CompletableFuture<Reservation> handleConcurrency(ReservationRequest.SaveDto requestDto, Model model, Portfolio portfolio) {
        String lockName = getLockName(requestDto);  // 락 이름
        RLock rlock = redissonClient.getLock(lockName);  // 락 생성

        Reservation reservationInfo = null;

        try {
            if (rlock.tryLock(10, TimeUnit.SECONDS)) {  // 1. 락을 선점합니다.

                // 2. 요청이 들어온 포트폴리오, 연도, 월, 일에 대해 예약 내역을 조회합니다.
                Optional<List<Reservation>> optionalReservations = reservationRepository.findByPortfolioAndYearAndMonthAndDay(
                        portfolio, requestDto.getYear(), requestDto.getMonth(), requestDto.getDay());

                // 3. 예약 내역이 존재하면, 시간대가 겹치는지 파악합니다.
                if (optionalReservations.isPresent()) {
                    List<Reservation> reservations = optionalReservations.get();
                    Set<String> existingTimes = new HashSet<>();
                    for (Reservation reservation : reservations)
                        existingTimes.addAll(Arrays.asList(reservation.getTimes().split(",")));

                    // 3-1. 이미 예약된 시간대와 겹친다면 예외를 반환합니다.
                    if (haveOverlappingTimes(existingTimes, requestDto.getTimes())) {
                        System.out.println("ConcurrentRequestHandler.handleConcurrency");
                        throw new ReservationConflictException(RESERVATION_CONFLICT);
                    }
                }

                // 4. 요청 받은 예약 내용을 DB에 저장합니다.
                reservationInfo = reservationRepository.save(
                        ReservationConverter.toReservationEntity(requestDto, model, portfolio)
                );

                // TODO: 4-1. 모델, 포트폴리오 내 예약 정보 추가
                // reservationInfo.pendingReservation(model, portfolio);

                // 5. 락을 해제합니다.
                rlock.unlock();

            } else {  // 락을 선점하지 못 했을 경우 InterruptedException 발생
                throw new InterruptedException();
            }
        } catch (InterruptedException e) {
            throw new ReservationException(RESERVATION_CANNOT_ACQUIRE_LOCK);
        }

        return CompletableFuture.completedFuture(reservationInfo).thenApply(reservation -> reservation);
    }

    private String getLockName(ReservationRequest.SaveDto request) {
        return request.getPortfolio_id() + "-" + request.getMonth() + "-" + request.getDay();
    }

    private boolean haveOverlappingTimes(Set<String> set1, Set<String> set2) {
        Set<String> a;
        Set<String> b;
        if (set1.size() <= set2.size()) {
            a = set1;
            b = set2;
        } else {
            a = set2;
            b = set1;
        }
        for (String s : a) {
            if (b.contains(s))
                return true;
        }
        return false;
    }
}
