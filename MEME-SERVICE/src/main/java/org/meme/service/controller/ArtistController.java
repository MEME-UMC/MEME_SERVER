package org.meme.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.BaseResponseDto;
import org.meme.domain.common.status.SuccessStatus;
import org.meme.service.service.ArtistService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArtistController {

    private final ArtistService artistService;

    @Operation(summary = "아티스트 프로필 조회(Model Ver.")
    @GetMapping("/profile/{userId}/{artistId}")
    public BaseResponseDto getArtistProfile(@PathVariable(name = "userId") Long userId, @PathVariable(name = "artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.ARTIST_PROFILE_GET, artistService.getArtistProfile(userId, artistId));
    }

    // TODO: AvailableTime
    @Operation(summary = "아티스트 예약 가능 시간 편집")
    @PatchMapping("/availabletime")
    public BaseResponseDto patchAvailableTime() {
        artistService.patchArtistAvailableTime();
        return BaseResponseDto.SuccessResponse(SuccessStatus.ARTIST_AVAILABLE_TIME_PATCH);
    }

    @Operation(summary = "아티스트 프로필 조회 (Artist Ver.)")
    @GetMapping("/profile/{artistId}")
    public BaseResponseDto getArtistProfile(@PathVariable(name = "artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.ARTIST_PROFILE_GET, artistService.getArtistProfileFromArtist(artistId));
    }
}
