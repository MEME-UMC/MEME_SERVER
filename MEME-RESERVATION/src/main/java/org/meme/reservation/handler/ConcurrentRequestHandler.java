package org.meme.reservation.handler;

import lombok.RequiredArgsConstructor;
import org.meme.domain.entity.Model;
import org.meme.domain.entity.Portfolio;
import org.meme.domain.entity.Reservation;
import org.meme.domain.repository.ReservationRepository;
import org.meme.reservation.converter.ReservationConverter;
import org.meme.reservation.dto.ReservationRequest;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;

@RequiredArgsConstructor
@Service
public class ConcurrentRequestHandler {

    private final ReservationRepository reservationRepository;
    private final RedissonClient redissonClient;
    private final Map<String, Set<String>> processedSlotsMap = new HashMap<>();

    @Transactional
    public void handleConcurrency(ReservationRequest.SaveDto requestDto, Model model, Portfolio portfolio) throws RuntimeException {
        // 쓰레드풀 생성을 위한 ExecutorService 객체 생성 (쓰레드 수를 CPU 코어 수로 제한)
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors);

        // 요청들을 담을 리스트 생성
        List<ReservationRequest.SaveDto> requests = List.of(requestDto);

        // Future 객체들을 담을 리스트 생성
        List<Future<Reservation>> reservationInfos = new ArrayList<>();

        // 각 요청을 처리
        for (ReservationRequest.SaveDto request : requests) {
            Future<Reservation> info = executorService.submit(() -> handleRequest(requestDto, model, portfolio));
            reservationInfos.add(info);
        }

        try {
            for (Future<Reservation> info : reservationInfos) {
                System.out.println(info.get().getTimes());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // ExecutorService 종료
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @Transactional
    public Reservation handleRequest(ReservationRequest.SaveDto requestDto, Model model, Portfolio portfolio) {
        // Lock 이름 생성 및 RLock 생성
        String lockName = getLockName(requestDto);
        RLock rlock = redissonClient.getLock(lockName);

        Reservation reservationInfo = null;

        try {
            if (rlock.tryLock(10, TimeUnit.SECONDS)) {  // 1. 락을 선점합니다.
                // 2. 예약 내역을 조회합니다. (portfolio_id, year, month, day)
                List<Reservation> reservations = reservationRepository.findByPortfolioAndYearAndMonthAndDay(portfolio, requestDto.getYear(), requestDto.getMonth(), requestDto.getDay())
                        .orElseThrow(() -> new RuntimeException("Could not find any reservations"));

                // 2-1. 예약 내역이 존재하면, 시간대를 파악합니다.
                if (!reservations.isEmpty()) {
                    Set<String> existingTimes = new HashSet<>();
                    for (Reservation reservation : reservations) {
                        String[] split = reservation.getTimes().split(",");
                        existingTimes.addAll(Arrays.asList(split));
                    }

                    // 이미 예약된 시간대라면
                    if (haveOverlappingTimes(existingTimes, requestDto.getTimes())) {
                        // System.out.println("Reservation has overlapping times");
                        throw new RuntimeException("Reservation has overlapping times");
                    }
                }

                // 3. 예약 내용을 DB에 저장합니다.
                Reservation reservation = ReservationConverter.toReservationEntity(requestDto, model, portfolio);
                reservationInfo = reservationRepository.save(reservation);

                // 4. 락을 해제합니다.
                rlock.unlock();

            } else {
                System.out.println("Cannot acquire lock");
            }
        } catch (InterruptedException | RuntimeException e) {
            e.printStackTrace();
        }

        return reservationInfo;
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
