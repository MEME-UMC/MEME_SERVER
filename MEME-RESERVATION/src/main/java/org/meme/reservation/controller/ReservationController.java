package org.meme.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.meme.domain.common.BaseResponseDto;
import org.meme.reservation.dto.ReservationRequest;
import org.meme.reservation.dto.ReservationResponse;
import org.meme.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.meme.domain.common.status.SuccessStatus.*;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약하기
     * @param requestDto
     * @param portfolioId
     * @return
     */
    @PostMapping("/api/v2/{portfolioId}/reservation")
    public BaseResponseDto<?> makeReservation(@RequestBody ReservationRequest.SaveDto requestDto, @PathVariable("portfolioId") Long portfolioId) {
        reservationService.makeReservation(requestDto, portfolioId);
        return BaseResponseDto.SuccessResponse(RESERVATION_SUCCESS);
    }

    /**
     * 예약 일정 조회
     * @param portfolioId
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/api/v2/{portfolioId}/schedule/{year}/{month}")
    public BaseResponseDto<ReservationResponse.ScheduleYearAndMonthDto> getScheduleByYearAndMonth(@PathVariable("portfolioId") Long portfolioId, @PathVariable("year") int year, @PathVariable("month") int month) {
        return BaseResponseDto.SuccessResponse(GET_SCHEDULE_SUCCESS, reservationService.getScheduleByYearAndMonth(portfolioId, year, month));
    }

    /**
     * 영업 가능 날짜 등록 (아티스트)
     * @param enableDateDto
     * @param artistId
     * @return
     */
    @PostMapping("/api/v2/mypage/{artistId}/enable/date")
    public BaseResponseDto<?> addEnableDate(@RequestBody ReservationRequest.EnableDateDto enableDateDto, @PathVariable("artistId") Long artistId) {
        reservationService.addEnableDate(enableDateDto, artistId);
        return BaseResponseDto.SuccessResponse(ADD_ENABLE_DATE_SUCCESS);
    }

    /**
     * 영업 가능 시간대 등록 (아티스트)
     * @param enableTimeDto
     * @param artistId
     * @return
     */
    @PostMapping("/api/v2/mypage/{artistId}/enable/time")
    public BaseResponseDto<?> addEnableTime(@RequestBody ReservationRequest.EnableTimeDto enableTimeDto, @PathVariable("artistId") Long artistId) {
        reservationService.addEnableTime(enableTimeDto, artistId);
        return BaseResponseDto.SuccessResponse(ADD_ENABLE_TIME_SUCCESS);
    }

    /**
     * 영업 가능 날짜 조회 (아티스트)
     * @param artistId
     * @return
     */
    @GetMapping("/api/v2/mypage/{artistId}/enable/date")
    public BaseResponseDto<ReservationResponse.DateDto> getEnableDate(@PathVariable("artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(GET_ENABLE_DATE_SUCCESS, reservationService.getEnableDate(artistId));
    }

    /**
     * 영업 가능 시간대 조회 (아티스트)
     * @param artistId
     * @return
     */
    @GetMapping("/api/v2/mypage/{artistId}/enable/time")
    public BaseResponseDto<ReservationResponse.TimeDto> getEnableTime(@PathVariable("artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(GET_ENABLE_TIME_SUCCESS, reservationService.getEnableTime(artistId));
    }

    /**
     * 영업 가능 날짜 수정 (아티스트)
     * @param enableDateUpdateDto
     * @param artistId
     * @return
     */
    @PatchMapping("/api/v2/mypage/{artistId}/enable/date")
    public BaseResponseDto<?> updateEnableDate(@RequestBody ReservationRequest.EnableDateDto enableDateUpdateDto, @PathVariable("artistId") Long artistId) {
        reservationService.updateEnableDate(enableDateUpdateDto, artistId);
        return BaseResponseDto.SuccessResponse(UPDATE_ENABLE_DATE_SUCCESS);
    }

    /**
     * 영업 가능 시간대 수정 (아티스트)
     * @param enableTimeUpdateDto
     * @param artistId
     * @return
     */
    @PatchMapping("/api/v2/mypage/{artistId}/enable/time")
    public BaseResponseDto<?> updateEnableTime(@RequestBody ReservationRequest.EnableTimeDto enableTimeUpdateDto, @PathVariable("artistId") Long artistId) {
        reservationService.updateEnableTime(enableTimeUpdateDto, artistId);
        return BaseResponseDto.SuccessResponse(UPDATE_ENABLE_TIME_SUCCESS);
    }

    @GetMapping("/api/v2/mypage/{artistId}/reservations")
    public BaseResponseDto<List<ReservationResponse.ReservationSimpleDto>> getReservationSimplesByArtist(@PathVariable("artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(RESERVATION_GET_BY_ARTIST, reservationService.getReservationSimplesByArtist(artistId));
    }

    @GetMapping("/api/v2/mypage/{modelId}/reservations")
    public BaseResponseDto<List<ReservationResponse.ReservationSimpleDto>> getReservationSimplesByModel(@PathVariable("modelId") Long modelId) {
        return BaseResponseDto.SuccessResponse(RESERVATION_GET_BY_MODEL, reservationService.getReservationSimplesByModel(modelId));
    }

}
