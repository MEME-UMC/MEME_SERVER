package org.meme.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.BaseResponseDto;
import org.meme.domain.common.status.SuccessStatus;
import org.meme.service.dto.ArtistRequest;
import org.meme.service.dto.MypageRequest;
import org.meme.service.service.MypageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class MypageController {
    private final MypageService mypageService;

    // model
    @Operation(summary = "모델 프로필 관리")
    @PatchMapping("/profile/model")
    public BaseResponseDto updateModelProfile(@RequestBody MypageRequest.ModelProfileDto modelProfileDto) {
        mypageService.updateModelProfile(modelProfileDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.MODEL_PROFILE_UPDATE);
    }

    @Operation(summary = "모델 프로필 관리 조회(수정 전 정보 불러오기 용)")
    @GetMapping("/profile/model/{userId}")
    public BaseResponseDto getModelProfile(@PathVariable(name = "userId") Long userId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.MODEL_PROFILE_GET, mypageService.getModelProfile(userId));
    }

    // artist
    @Operation(summary = "아티스트 프로필 관리")
    @PatchMapping("/profile/artist")
    public BaseResponseDto updateArtistProfile(@RequestBody MypageRequest.ArtistProfileDto profileDto) {
        mypageService.updateArtistProfile(profileDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.ARTIST_PROFILE_UPDATE);
    }

    @Operation(summary = "아티스트 프로필 관리 조회(수정 전 정보 불러오기 용)")
    @GetMapping("/profile/artist/{userId}")
    public BaseResponseDto getArtistProfile(@PathVariable(name = "userId") Long userId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.ARTIST_PROFILE_GET, mypageService.getProfile(userId));
    }

    // mypage
    @Operation(summary = "마이페이지 조회")
    @GetMapping("/profile/{userId}")
    public BaseResponseDto getProfile(@PathVariable(name = "userId") Long userId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.MYPAGE_GET, mypageService.getProfile(userId));
    }

    // inquiry
    @Operation(summary = "문의하기", description = "문의하기 API입니다.")
    @PostMapping("/contact")
    public BaseResponseDto contact(@RequestBody MypageRequest.InquiryDto mypageInquiryDto) {
        mypageService.createInquiry(mypageInquiryDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.CONTACT_CREATE);
    }

    @Operation(summary = "문의 조회하기", description = "문의 조회하기 API입니다.")
    @GetMapping("/contact/{userId}")
    public BaseResponseDto getContact(@PathVariable(name = "userId") Long userId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.CONTACT_GET, mypageService.getInquiry(userId));
    }
}
