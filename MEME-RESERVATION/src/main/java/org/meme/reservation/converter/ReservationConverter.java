package org.meme.reservation.converter;

import org.meme.reservation.domain.*;
import org.meme.reservation.dto.ReservationRequest;
import org.meme.reservation.dto.ReservationResponse;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservationConverter {

    final static String SPLIT_COMMA = ",";

    public static Reservation toReservationEntity(ReservationRequest.SaveDto saveDto, Model model, Portfolio portfolio) {
        return Reservation.builder()
                .model(model)
                .portfolio(portfolio)
                .month(saveDto.getMonth())
                .year(saveDto.getYear())
                .day(saveDto.getDay())
                .times(intoString(saveDto.getTimes()))
                .status(Status.PENDING)
                .location(saveDto.getLocation())
                .artistName(portfolio.getUser().getNickname())
                .makeupName(portfolio.getMakeupName())
                .price(portfolio.getPrice())
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
                .artistId(artistEnableDate.getArtist().getUserId())
                .enableDates(intoDates(artistEnableDate.getEnableDates()))
                .build();
    }

    public static ReservationResponse.TimeDto toTimeDto(ArtistEnableTime artistEnableTime) {
        return ReservationResponse.TimeDto.builder()
                .artistId(artistEnableTime.getArtist().getUserId())
                .enableTimes(intoTimes(artistEnableTime.getEnableTimes()))
                .build();
    }

    private static String intoString(Set<String> times) {
        StringJoiner joiner = new StringJoiner(SPLIT_COMMA);
        for (String time : times) {
            joiner.add(time);
        }
        return joiner.toString();
    }

    public static String intoDateString(List<LocalDate> dates) {
        StringJoiner joiner = new StringJoiner(SPLIT_COMMA);
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
            StringJoiner timeJoiner = new StringJoiner(SPLIT_COMMA);
            for (String time : times) {
                timeJoiner.add(time);
            }
            mainJoiner.add(day.name() + "_" + timeJoiner.toString());
        }

        return mainJoiner.toString();
    }

    public static List<LocalDate> intoDates(String enableDates) {
        List<LocalDate> dates = new ArrayList<>();

        String[] split = enableDates.split(SPLIT_COMMA);
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
            List<String> timesInDay = Arrays.asList(timesString.split(SPLIT_COMMA));

            times.put(day, timesInDay);
        }

        return times;
    }

    public static ReservationResponse.ReservationSimpleDto toReservationSimpleDto(Reservation reservation, Role role) {
        String[] reservationTimes = getReservationTimes(reservation.getTimes());
        String startTime = reservationTimes[0];
        String endTime = reservationTimes[reservationTimes.length - 1];

        int year = reservation.getYear();
        int month = reservation.getMonth();
        int day = reservation.getDay();

        String userName = "";
        if (role == Role.ARTIST) {
            userName = reservation.getModel().getUser().getUsername();
        } else if (role == Role.MODEL) {
            userName = reservation.getArtistName();
        } else {
            // exception
        }
        return getReservationSimpleDto(reservation, startTime, endTime, year, month, day, userName);
    }

    private static ReservationResponse.ReservationSimpleDto getReservationSimpleDto(Reservation reservation, String startTime, String endTime, int year, int month, int day, String userName) {
        return ReservationResponse.ReservationSimpleDto.builder()
                .reservationId(reservation.getReservationId())
                .makeupName(reservation.getMakeupName())
                .userName(userName)
                .location(reservation.getLocation())
                .price(reservation.getPrice())
                .status(reservation.getStatus())
                .year(reservation.getYear())
                .month(reservation.getMonth())
                .day(reservation.getDay())
                .dayOfWeek(getDayOfWeek(LocalDate.of(year, month, day)))
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    private static String[] getReservationTimes(String times) {
        return times.split(SPLIT_COMMA);
    }

    private static String getDateString(Reservation reservation) {
        return reservation.getYear() + "년 " + reservation.getMonth() + "월 " + reservation.getDay() + "일";
    }

    private static String getTimeString(Reservation reservation) {
        String[] reservationTimes = getReservationTimes(reservation.getTimes());
        return reservationTimes[0] + " - " + reservationTimes[reservationTimes.length - 1];
    }

    private static String getPriceString(Reservation reservation) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        return formatter.format(reservation.getPrice()) + "원";
    }

    public static ReservationResponse.ReservationDetailArtistSightDto toReservationDetailArtistSightDto(Reservation reservation) {
        Model model = reservation.getModel();

        return ReservationResponse.ReservationDetailArtistSightDto.builder()
                .reservationId(reservation.getReservationId())

                // TODO: 참조가 한번 더 들어가기 때문에 고민
                .modelName(model.getUser().getNickname())
                .modelEmail(model.getUser().getEmail())
                .modelGender(model.getUser().getGender())

                .modelSkinType(model.getSkinType())
                .modelPersonalColor(model.getPersonalColor())
                .reservationName(reservation.getMakeupName())
                .reservationDate(getDateString(reservation))
                .reservationTime(getTimeString(reservation))
                .reservationPrice(getPriceString(reservation))
                .reservationStatus(reservation.getStatus())
                .build();
    }

    public static ReservationResponse.ReservationDetailModelSightDto toReservationDetailModelSightDto(Reservation reservation) {
        User artist = reservation.getPortfolio().getUser();

        return ReservationResponse.ReservationDetailModelSightDto.builder()
                .reservationId(reservation.getReservationId())

                // TODO: 참조가 살짝 복잡한 것 같아서 고민
                .aritstName(artist.getNickname())
                .artistEmail(artist.getEmail())

                .reservationName(reservation.getMakeupName())
                .reservationDate(getDateString(reservation))
                .reservationTime(getTimeString(reservation))
                .reservationPrice(getPriceString(reservation))
                .reservationStatus(reservation.getStatus())
                .build();
    }

    private static String getDayOfWeek(LocalDate localDate) {
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return switch (dayOfWeek) {
            case SUNDAY -> DayOfWeek.SUN.getDayOfWeekName();
            case MONDAY -> DayOfWeek.MON.getDayOfWeekName();
            case TUESDAY -> DayOfWeek.TUE.getDayOfWeekName();
            case WEDNESDAY -> DayOfWeek.WED.getDayOfWeekName();
            case THURSDAY -> DayOfWeek.THU.getDayOfWeekName();
            case FRIDAY -> DayOfWeek.FRI.getDayOfWeekName();
            case SATURDAY -> DayOfWeek.SAT.getDayOfWeekName();
        };
    }
}
