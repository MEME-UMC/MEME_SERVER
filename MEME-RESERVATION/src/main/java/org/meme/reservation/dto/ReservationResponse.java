package org.meme.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.reservation.domain.*;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationSimpleDto {
        private String makeupName;
        private String artistName;
        private String location;
        private int price;
        private int year;
        private int month;
        private String startTime;
        private String endTime;
        private Status status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationDetailArtistSightDto {
        // 아티스트에게 보이는 모델 정보
        private String model_name;
        private String model_email;
        private Gender model_gender;
        private SkinType model_skin_type;
        private PersonalColor model_personal_color;

        // 공통 정보
        private String reservation_name;
        private String reservation_date;   // 2024년 08월 11일
        private String reservation_time;   // 18:00 - 19:00
        private String reservation_price;  // 100,000원
        private Status reservation_status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationDetailModelSightDto {
        // 모델에게 보이는 아티스트 정보
        private String aritst_name;
        private String artist_email;

        // 공통 정보
        private String reservation_name;
        private String reservation_date;   // 8월 11일 (일)
        private String reservation_time;   // 18:00 - 19:00
        private String reservation_price;  // 100,000원
        private Status reservation_status;
    }
}
