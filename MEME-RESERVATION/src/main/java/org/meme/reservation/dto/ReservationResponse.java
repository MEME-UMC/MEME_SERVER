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
        private Long artistId;
        private List<LocalDate> enableDates;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeDto {
        private Long artistId;
        private Map<DayOfWeek, List<String>> enableTimes;
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
        private Long reservationId;
        private String makeupName;
        private String userName;
        private String location;
        private int price;
        private int year;
        private int month;
        private int day;
        private String dayOfWeek;
        private String startTime;
        private String endTime;
        private Status status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationDetailArtistSightDto {
        private Long reservationId;

        // 아티스트에게 보이는 모델 정보
        private String modelName;
        private String modelEmail;
        private Gender modelGender;
        private SkinType modelSkinType;
        private PersonalColor modelPersonalColor;

        // 공통 정보
        private String reservationName;
        private String reservationDate;   // 2024년 08월 11일
        private String reservationTime;   // 18:00 - 19:00
        private String reservationPrice;  // 100,000원
        private Status reservationStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationDetailModelSightDto {
        private Long reservationId;

        // 모델에게 보이는 아티스트 정보
        private String aritstName;
        private String artistEmail;

        // 공통 정보
        private String reservationName;
        private String reservationDate;   // 8월 11일 (일)
        private String reservationTime;   // 18:00 - 19:00
        private String reservationPrice;  // 100,000원
        private Status reservationStatus;
    }

    // Status, 날짜(year, month, day), startTime 오름차순 정렬 메서드
    public static void sortByStatusAndDate(List<ReservationSimpleDto> reservations) {
        reservations.sort((r1, r2) -> {
            // Status 비교
            int statusCompare = Integer.compare(getOrder(r1.status), getOrder(r2.status));
            if (statusCompare != 0) {
                return statusCompare;
            }

            // year, month, day 비교
            int yearCompare = Integer.compare(r1.year, r2.year);
            if (yearCompare != 0) {
                return yearCompare;
            }

            int monthCompare = Integer.compare(r1.month, r2.month);
            if (monthCompare != 0) {
                return monthCompare;
            }

            int dayCompare = Integer.compare(r1.day, r2.day);
            if (dayCompare != 0) {
                return dayCompare;
            }

            // startTime 비교 (시간 문자열이므로 사전순 비교)
            return r1.startTime.compareTo(r2.startTime);
        });
    }

    // Status 우선순위를 반환하는 헬퍼 메서드
    private static int getOrder(Status status) {
        switch (status) {
            case APPROVED:
                return 1;
            case PENDING:
                return 2;
            case COMPLETED:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown status: " + status);
        }
    }

}
