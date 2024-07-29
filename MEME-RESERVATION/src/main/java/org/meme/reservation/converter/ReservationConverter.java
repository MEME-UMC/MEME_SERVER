package org.meme.reservation.converter;

import org.meme.domain.entity.*;
import org.meme.domain.enums.DayOfWeek;
import org.meme.reservation.dto.ReservationRequest;
import org.meme.reservation.dto.ReservationResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservationConverter {

    public static Reservation toReservationEntity(ReservationRequest.SaveDto saveDto, Model model, Portfolio portfolio) {
        return Reservation.builder()
                .model(model)
                .portfolio(portfolio)
                .month(saveDto.getMonth())
                .year(saveDto.getYear())
                .day(saveDto.getDay())
                .times(intoString(saveDto.getTimes()))
                .status(saveDto.getStatus())
                .location(saveDto.getLocation())
                .build();
    }

    public static ArtistEnableDate toEnableDateEntity(ReservationRequest.EnableDateDto enableDateDto, Artist artist) {
        return ArtistEnableDate.builder()
                .artist(artist)
                .year(enableDateDto.getYear())
                .month(enableDateDto.getMonth())
                .enableDates(ReservationConverter.intoDateString(enableDateDto.getEnable_date()))
                .build();
    }

    public static ArtistEnableTime toEnableTimeEntity(ReservationRequest.EnableTimeDto enableTimeDto, Artist artist) {
        return ArtistEnableTime.builder()
                .artist(artist)
                .enableTimes(ReservationConverter.intoTimeString(enableTimeDto.getEnable_time()))
                .build();
    }

    public static ReservationResponse.SuccessDto toSuccessDto(Reservation reservation) {
        return ReservationResponse.SuccessDto.builder()
                .year(reservation.getYear())
                .month(reservation.getMonth())
                .day(reservation.getDay())
                .build();
    }

    public static ReservationResponse.DateDto toDateDto(ArtistEnableDate artistEnableDate) {
        return ReservationResponse.DateDto.builder()
                .artist_id(artistEnableDate.getArtist().getUserId())
                .enable_dates(intoDates(artistEnableDate.getEnableDates()))
                .build();
    }

    public static ReservationResponse.TimeDto toTimeDto(ArtistEnableTime artistEnableTime) {
        return ReservationResponse.TimeDto.builder()
                .artist_id(artistEnableTime.getArtist().getUserId())
                .enable_times(intoTimes(artistEnableTime.getEnableTimes()))
                .build();
    }

    private static String intoString(Set<String> times) {
        StringJoiner joiner = new StringJoiner(",");
        for (String time : times) {
            joiner.add(time);
        }
        return joiner.toString();
    }

    public static String intoDateString(List<LocalDate> dates) {
        StringJoiner joiner = new StringJoiner(",");
        for (LocalDate date : dates) {
            joiner.add(date.toString());
        }
        return joiner.toString();
    }

    public static String intoTimeString(Map<DayOfWeek, List<String>> enableTimes) {
        StringJoiner mainJoiner = new StringJoiner(";");

        for (Map.Entry<DayOfWeek, List<String>> entry : enableTimes.entrySet()) {
            DayOfWeek day = entry.getKey();
            List<String> times = entry.getValue();
            StringJoiner timeJoiner = new StringJoiner(",");
            for (String time : times) {
                timeJoiner.add(time);
            }
            mainJoiner.add(day.name() + "_" + timeJoiner.toString());
        }

        return mainJoiner.toString();
    }

    public static List<LocalDate> intoDates(String enableDates) {
        List<LocalDate> dates = new ArrayList<>();

        String[] split = enableDates.split(",");
        for (String date : split) {
            dates.add(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE));
        }

        return dates;
    }

    public static Map<DayOfWeek, List<String>> intoTimes(String enableTimes) {
        Map<DayOfWeek, List<String>> times = new HashMap<>();

        String[] dayEntries = enableTimes.split(";");
        for (String dayEntry : dayEntries) {
            String[] parts = dayEntry.split("_");
            String dayString = parts[0];
            String timesString = parts[1];

            DayOfWeek day = DayOfWeek.valueOf(dayString);
            List<String> timesInDay = Arrays.asList(timesString.split(","));

            times.put(day, timesInDay);
        }

        return times;
    }
}
