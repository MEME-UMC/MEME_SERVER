package org.meme.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.DayOfWeek;
import org.meme.domain.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReservationRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveDto {
        private Long model_id;
        private Long portfolio_id;
        private int year;
        private int month;
        private int day;
        private Set<String> times;
        private Status status;
        private String location;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnableDateDto {
        private int year;
        private int month;
        private List<LocalDate> enable_date;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnableTimeDto {
        private Map<DayOfWeek, List<String>> enable_time;
    }
}
