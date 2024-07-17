package org.meme.reservation.handler;

import lombok.RequiredArgsConstructor;
import org.meme.domain.enums.DayOfWeek;
import org.meme.domain.repository.ArtistEnableDateRepository;
import org.meme.domain.repository.ArtistEnableTimeRepository;
import org.meme.reservation.dto.ReservationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ScheduleHandler {

    private final ArtistEnableDateRepository enableDateRepository;
    private final ArtistEnableTimeRepository enableTimeRepository;

    public ReservationResponse.ScheduleYearAndMonthDto getSchedule(Set<LocalDate> businessDays, Map<DayOfWeek, List<String>> workTimes, Map<Integer, List<String>> reservations, int year, int month) {
        // 반환에 필요한 객체를 선언합니다.
        ReservationResponse.ScheduleYearAndMonthDto dto = new ReservationResponse.ScheduleYearAndMonthDto();
        Map<Integer, List<ReservationResponse.ScheduleDayDto>> scheduleDayDtoMap = new HashMap<>();

        // 특정 연도와 월에 대해 전체 일수를 구합니다. (e.g. 2024-07 -> 31)
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int day = 1; day <= daysInMonth; day++) {
            // 날짜와 요일을 가져옵니다.
            LocalDate startDay = LocalDate.of(year, month, day);
            DayOfWeek dayOfWeek = getDayOfWeek(startDay);

            System.out.println("startDay = " + startDay);
            System.out.println("dayOfWeek = " + dayOfWeek);

            // 날짜에 해당하는 요일에 대해 영업 가능 시간대를 가져옵니다.
            Set<String> workTimeSet = Set.copyOf(workTimes.get(dayOfWeek));

            // 해당 날짜에 모든 예약을 가져옵니다.
            Set<String> reservationSet;
            if (reservations.get(day) != null)  // 예약이 있을 때만 가져옵니다.
                reservationSet = Set.copyOf(reservations.get(day));
            else
                reservationSet = new HashSet<>();  // empty set으로 초기화

            List<ReservationResponse.ScheduleDayDto> scheduleDayDtos = new ArrayList<>();

            LocalTime time = LocalTime.of(0, 0);
            System.out.println("time = " + time);
            // System.out.println("time.equals(LocalTime.of(23, 30) = " + time.equals(LocalTime.of(23, 30)));
            // System.out.println("time.plusMinutes(30) = " + time.plusMinutes(30));

            while (!time.equals(LocalTime.of(23, 30))) {
                boolean businessDay;
                boolean workTime;
                boolean saleTime;

                String startTime = time.format(formatter);
                System.out.println("startTime = " + startTime);

                // 1. isBusinessDay 확인
                if (businessDays.contains(startDay)) {
                    businessDay = true;
                } else {
                    System.out.println("ScheduleHandler.getSchedule isBusinessDay false");
                    scheduleDayDtos.add(getScheduleDayDto(false, false, false, startDay, startTime));
                    time = time.plusMinutes(30);
                    continue;
                }

                // 2. isWorkTime 확인
                if (workTimeSet.contains(startTime)) {
                    workTime = true;
                } else {
                    System.out.println("ScheduleHandler.getSchedule isWorkTime false");
                    scheduleDayDtos.add(getScheduleDayDto(true, false, false, startDay, startTime));
                    time = time.plusMinutes(30);
                    continue;
                }

                // 3. isSaleTime 확인
                if (!reservationSet.contains(time.format(formatter))) {
                    System.out.println("ScheduleHandler.getSchedule isSaleTime true");// 예약이 없을 때
                    scheduleDayDtos.add(getScheduleDayDto(true, true, true, startDay, startTime));
                    time = time.plusMinutes(30);
                } else {
                    System.out.println("ScheduleHandler.getSchedule isSaleTime false");
                    scheduleDayDtos.add(getScheduleDayDto(true, true, false, startDay, startTime));
                    time = time.plusMinutes(30);
                }
            }

            scheduleDayDtos.size();

            scheduleDayDtoMap.put(day, scheduleDayDtos);
        }

        return ReservationResponse.ScheduleYearAndMonthDto.builder()
                .year(year)
                .month(month)
                .reservations(scheduleDayDtoMap)
                .build();
    }

    private DayOfWeek getDayOfWeek(LocalDate localDate) {
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return switch (dayOfWeek) {
            case SUNDAY -> DayOfWeek.SUN;
            case MONDAY -> DayOfWeek.MON;
            case TUESDAY -> DayOfWeek.TUE;
            case WEDNESDAY -> DayOfWeek.WED;
            case THURSDAY -> DayOfWeek.THU;
            case FRIDAY -> DayOfWeek.FRI;
            case SATURDAY -> DayOfWeek.SAT;
        };
    }

    private ReservationResponse.ScheduleDayDto getScheduleDayDto(boolean businessDay, boolean workTime, boolean saleTime, LocalDate startDay, String startTime) {
        ReservationResponse.ScheduleDayDto scheduleDayDto = new ReservationResponse.ScheduleDayDto();
        scheduleDayDto.setBusinessDay(businessDay);
        scheduleDayDto.setWorkTime(workTime);
        scheduleDayDto.setSaleTime(saleTime);
        scheduleDayDto.setUnitStartDay(startDay);
        scheduleDayDto.setUnitStartTime(startTime);
        return scheduleDayDto;
    }
}
