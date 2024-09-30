package org.meme.service.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.meme.service.common.BaseResponseDto;
import org.meme.service.common.status.SuccessStatus;
import org.meme.service.domain.service.ArtistService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ArtistController {

    private final ArtistService artistService;

    @Operation(summary = "아티스트 프로필 조회(Model Ver.")
    @GetMapping("/profile/{userId}/{artistId}")
    public BaseResponseDto getArtistProfile(@PathVariable(name = "userId") Long userId, @PathVariable(name = "artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.ARTIST_PROFILE_GET, artistService.getArtistProfile(userId, artistId));
    }

    @Operation(summary = "아티스트 프로필 조회 (Artist Ver.)")
    @GetMapping("/profile/{artistId}")
    public BaseResponseDto getArtistProfile(@PathVariable(name = "artistId") Long artistId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.ARTIST_PROFILE_GET, artistService.getArtistProfileFromArtist(artistId));
    }
}
