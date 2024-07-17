package org.meme.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.meme.domain.common.BaseResponseDto;
import org.meme.domain.common.status.SuccessStatus;
import org.meme.reservation.dto.ReservationRequest;
import org.meme.reservation.dto.ReservationResponse;
import org.meme.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/api/v2/{portfolioId}/reservation")
    public void makeReservation(@RequestBody ReservationRequest.SaveDto requestDto, @PathVariable("portfolioId") Long portfolioId) {
        reservationService.makeReservation(requestDto, portfolioId);
    }

    @GetMapping("/api/v2/{portfolioId}/schedule/{year}/{month}")
    public BaseResponseDto<ReservationResponse.ScheduleYearAndMonthDto> getScheduleByYearAndMonth(@PathVariable("portfolioId") Long portfolioId, @PathVariable("year") int year, @PathVariable("month") int month) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.GET_SCHEDULE_SUCCESS, reservationService.getScheduleByYearAndMonth(portfolioId, year, month));
    }

    // 아티스트 가능 날짜 등록
    @PostMapping("/api/v2/mypage/{artistId}/enable/date")
    public BaseResponseDto<?> addEnableDate(@RequestBody ReservationRequest.EnableDateDto enableDateDto, @PathVariable("artistId") Long artistId) {
        reservationService.addEnableDate(enableDateDto, artistId);
        return BaseResponseDto.SuccessResponse(SuccessStatus.ADD_ENABLE_DATE_SUCCESS);
    }

    // 아티스트 가능 시간대 등록
    @PostMapping("/api/v2/mypage/{artistId}/enable/time")
    public BaseResponseDto<?> addEnableTime(@RequestBody ReservationRequest.EnableTimeDto enableTimeDto, @PathVariable("artistId") Long artistId) {
        reservationService.addEnableTime(enableTimeDto, artistId);
        return BaseResponseDto.SuccessResponse(SuccessStatus.ADD_ENABLE_TIME_SUCCESS);
    }

    // 아티스트 가능 날짜 조회
    @GetMapping("/api/v2/mypage/{artistId}/enable/date")
    public BaseResponseDto<ReservationResponse.DateDto> getEnableDate(@PathVariable("artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.GET_ENABLE_DATE_SUCCESS, reservationService.getEnableDate(artistId));
    }

    // 아티스트 가능 시간대 조회
    @GetMapping("/api/v2/mypage/{artistId}/enable/time")
    public BaseResponseDto<ReservationResponse.TimeDto> getEnableTime(@PathVariable("artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.GET_ENABLE_TIME_SUCCESS, reservationService.getEnableTime(artistId));
    }

    @PatchMapping("/api/v2/mypage/{artistId}/enable/date")
    public BaseResponseDto<?> updateEnableDate(@RequestBody ReservationRequest.EnableDateDto enableDateUpdateDto, @PathVariable("artistId") Long artistId) {
        reservationService.updateEnableDate(enableDateUpdateDto, artistId);
        return BaseResponseDto.SuccessResponse(SuccessStatus.UPDATE_ENABLE_DATE_SUCCESS);
    }

    @PatchMapping("/api/v2/mypage/{artistId}/enable/time")
    public BaseResponseDto<?> updateEnableTime(@RequestBody ReservationRequest.EnableTimeDto enableTimeUpdateDto, @PathVariable("artistId") Long artistId) {
        reservationService.updateEnableTime(enableTimeUpdateDto, artistId);
        return BaseResponseDto.SuccessResponse(SuccessStatus.UPDATE_ENABLE_TIME_SUCCESS);
    }

    // 아티스트 예약 상태 변경 (승낙, 거절 등)
}
