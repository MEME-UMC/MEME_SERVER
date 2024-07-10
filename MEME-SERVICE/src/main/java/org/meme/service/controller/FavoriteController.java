package org.meme.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.BaseResponseDto;
import org.meme.domain.common.status.SuccessStatus;
import org.meme.service.dto.FavoriteRequest;
import org.meme.service.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Operation(summary = "관심 아티스트 조회", description = "관심 아티스트를 조회하는 API입니다.")
    @GetMapping("/artist/{modelId}")
    public BaseResponseDto getFavoriteArtist(@PathVariable(name = "modelId") Long modelId, @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.FAVORITE_ARTIST_GET, favoriteService.getFavoriteArtist(modelId, page));
    }

    @Operation(summary = "관심 메이크업 조회", description = "관심 메이크업을 조회하는 API입니다.")
    @GetMapping("/portfolio/{modelId}")
    public BaseResponseDto getFavoritePortfolio(@PathVariable(name = "modelId") Long modelId, @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.FAVORITE_PORTFOLIO_GET, favoriteService.getFavoritePortfolio(modelId, page));
    }

    @Operation(summary = "관심 아티스트 추가", description = "관심 아티스트를 추가하는 API입니다.")
    @PostMapping("/artist")
    public BaseResponseDto postFavoriteArtist(@RequestBody FavoriteRequest.FavoriteArtistDto favoriteArtistDto) {
        favoriteService.addFavoriteArtist(favoriteArtistDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.FAVORITE_ARTIST_POST);
    }

    @Operation(summary = "관심 메이크업 추가", description = "관심 메이크업을 추가하는 API입니다.")
    @PostMapping("/portfolio")
    public BaseResponseDto postFavoritePortfolio(@RequestBody FavoriteRequest.FavoritePortfolioDto favoritePortfolioDto) {
        favoriteService.addFavoritePortfolio(favoritePortfolioDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.FAVORITE_PORTFOLIO_POST);
    }

    @Operation(summary = "관심 아티스트 삭제", description = "관심 아티스트를 삭제하는 API입니다.")
    @DeleteMapping("/artist")
    public BaseResponseDto deleteFavoriteArtist(@RequestBody FavoriteRequest.FavoriteArtistDto favoriteArtistDto) {
        favoriteService.deleteFavoriteArtist(favoriteArtistDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.FAVORITE_ARTIST_DELETE);
    }

    @Operation(summary = "관심 메이크업 삭제", description = "관심 메이크업을 삭제하는 API입니다.")
    @DeleteMapping("/portfolio")
    public BaseResponseDto deleteFavoritePortfolio(@RequestBody FavoriteRequest.FavoritePortfolioDto favoritePortfolioDto) {
        favoriteService.deleteFavoritePortfolio(favoritePortfolioDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.FAVORITE_PORTFOLIO_DELETE);
    }


}
