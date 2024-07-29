package org.meme.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.DayOfWeek;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReservationResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuccessDto {
        private int year;
        private int month;
        private int day;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateDto {
        private Long artist_id;
        private List<LocalDate> enable_dates;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeDto {
        private Long artist_id;
        private Map<DayOfWeek, List<String>> enable_times;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleYearAndMonthDto {
        private int year;
        private int month;
        private Map<Integer, List<ScheduleDayDto>> reservations;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDayDto {
        private boolean businessDay;   // 영업 가능 여부
        private boolean workTime;      // 영업 시간 여부
        private boolean saleTime;      // 예약 가능 여부
        private LocalDate unitStartDay;  // 단위 날짜
        private String unitStartTime;    // 단위 시간
    }
}
