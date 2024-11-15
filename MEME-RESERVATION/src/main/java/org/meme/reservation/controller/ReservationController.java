package org.meme.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.meme.reservation.common.BaseResponseDto;
import org.meme.reservation.dto.ReservationRequest;
import org.meme.reservation.dto.ReservationResponse;
import org.meme.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.meme.reservation.common.status.SuccessStatus.*;

@RequiredArgsConstructor
@RestController("/api/v2")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약하기
     * @param requestDto
     * @return
     */
    @PostMapping("/reservation")
    public BaseResponseDto<?> makeReservation(@RequestBody ReservationRequest.SaveDto requestDto) {
        reservationService.makeReservation(requestDto);
        return BaseResponseDto.SuccessResponse(RESERVATION_SUCCESS);
    }

    /**
     * 예약 일정 조회
     * @param portfolioId
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/{portfolioId}/schedule/{year}/{month}")
    public BaseResponseDto<ReservationResponse.ScheduleYearAndMonthDto> getScheduleByYearAndMonth(@PathVariable("portfolioId") Long portfolioId, @PathVariable("year") int year, @PathVariable("month") int month) {
        return BaseResponseDto.SuccessResponse(GET_SCHEDULE_SUCCESS, reservationService.getScheduleByYearAndMonth(portfolioId, year, month));
    }

    /**
     * 영업 가능 날짜 등록 (아티스트)
     * @param enableDateDto
     * @param artistId
     * @return
     */
    @PostMapping("/mypage/{artistId}/enable/date")
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
    @PostMapping("/mypage/{artistId}/enable/time")
    public BaseResponseDto<?> addEnableTime(@RequestBody ReservationRequest.EnableTimeDto enableTimeDto, @PathVariable("artistId") Long artistId) {
        reservationService.addEnableTime(enableTimeDto, artistId);
        return BaseResponseDto.SuccessResponse(ADD_ENABLE_TIME_SUCCESS);
    }

    /**
     * 영업 가능 날짜 조회 (아티스트)
     * @param artistId
     * @return
     */
    @GetMapping("/mypage/{artistId}/enable/date")
    public BaseResponseDto<ReservationResponse.DateDto> getEnableDate(@PathVariable("artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(GET_ENABLE_DATE_SUCCESS, reservationService.getEnableDate(artistId));
    }

    /**
     * 영업 가능 시간대 조회 (아티스트)
     * @param artistId
     * @return
     */
    @GetMapping("/mypage/{artistId}/enable/time")
    public BaseResponseDto<ReservationResponse.TimeDto> getEnableTime(@PathVariable("artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(GET_ENABLE_TIME_SUCCESS, reservationService.getEnableTime(artistId));
    }

    /**
     * 영업 가능 날짜 수정 (아티스트)
     * @param enableDateUpdateDto
     * @param artistId
     * @return
     */
    @PatchMapping("/mypage/{artistId}/enable/date")
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
    @PatchMapping("/mypage/{artistId}/enable/time")
    public BaseResponseDto<?> updateEnableTime(@RequestBody ReservationRequest.EnableTimeDto enableTimeUpdateDto, @PathVariable("artistId") Long artistId) {
        reservationService.updateEnableTime(enableTimeUpdateDto, artistId);
        return BaseResponseDto.SuccessResponse(UPDATE_ENABLE_TIME_SUCCESS);
    }

    /**
     * 예약 간단 조회 (아티스트)
     * @param artistId
     * @return
     */
    @GetMapping("/reservation/artist/{artistId}")
    public BaseResponseDto<List<ReservationResponse.ReservationSimpleDto>> getReservationSimplesByArtist(@PathVariable("artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(RESERVATION_GET_BY_ARTIST, reservationService.getReservationSimplesByArtist(artistId));
    }

    /**
     * 예약 간단 조회 (모델)
     * @param modelId
     * @return
     */
    @GetMapping("/reservation/model/{modelId}")
    public BaseResponseDto<List<ReservationResponse.ReservationSimpleDto>> getReservationSimplesByModel(@PathVariable("modelId") Long modelId) {
        return BaseResponseDto.SuccessResponse(RESERVATION_GET_BY_MODEL, reservationService.getReservationSimplesByModel(modelId));
    }

    /**
     * 예약 상세 조회 (아티스트)
     * @param reservationId
     * @return
     */
    @GetMapping("/reservation/artist/{reservationId}")
    public BaseResponseDto<ReservationResponse.ReservationDetailArtistSightDto> getReservationDetailByArtist(@PathVariable("reservationId") Long reservationId) {
        return BaseResponseDto.SuccessResponse(RESERVATION_DETAIL_GET_BY_ARTIST, reservationService.getReservationDetailByArtist(reservationId));
    }

    /**
     * 예약 상세 조회 (모델)
     * @param reservationId
     * @return
     */
    @GetMapping("/reservation/model/{reservationId}")
    public BaseResponseDto<ReservationResponse.ReservationDetailModelSightDto> getReservationDetailByModel(@PathVariable("reservationId") Long reservationId) {
        return BaseResponseDto.SuccessResponse(RESERVATION_DETAIL_GET_BY_MODEL, reservationService.getReservationDetailByModel(reservationId));
    }

    /**
     * 예약 승인 (아티스트)
     * @param reservationId
     * @return
     */
    @PatchMapping("/reservation/artist/{reservationId}/approved")
    public BaseResponseDto<?> changeStatusApprovedByArtist(@PathVariable("reservationId") Long reservationId) {
        reservationService.changeReservationStatusApproved(reservationId);
        return BaseResponseDto.SuccessResponse(RESERVATION_STATUS_APPROVED_BY_ARTIST);
    }

    /**
     * 예약 취소 (아티스트)
     * @param reservationId
     * @return
     */
    @PatchMapping("/reservation/artist/{reservationId}/canceled")
    public BaseResponseDto<?> changeStatusCanceledByArtist(@PathVariable("reservationId") Long reservationId) {
        reservationService.changeReservationStatusCanceled(reservationId);
        return BaseResponseDto.SuccessResponse(RESERVATION_STATUS_CANCELED_BY_ARTIST);
    }

    /**
     * 예약 취소 (모델)
     * @param reservationId
     * @return
     */
    @PatchMapping("/reservation/model/{reservationId}/canceled")
    public BaseResponseDto<?> changeStatusCanceledByModel(@PathVariable("reservationId") Long reservationId) {
        reservationService.changeReservationStatusCanceled(reservationId);
        return BaseResponseDto.SuccessResponse(RESERVATION_STATUS_CANCELED_BY_MODEL);
    }
}
