package org.meme.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.BaseResponseDto;
import org.springframework.web.bind.annotation.*;
import org.meme.service.dto.PortfolioRequest.*;
import org.meme.service.service.PortfolioService;
import org.meme.domain.common.status.SuccessStatus;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {
    private final PortfolioService portfolioService;

    @Operation(summary = "포트폴리오 생성", description = "포트폴리오를 생성하는 API입니다.")
    @PostMapping()
    public BaseResponseDto createPortfolio(@RequestBody CreatePortfolioDto portfolioDto){
        return BaseResponseDto.SuccessResponse(SuccessStatus.PORTFOLIO_CREATE, portfolioService.createPortfolio(portfolioDto));
    }

    @Operation(summary = "포트폴리오 전체 조회", description = "포트폴리오 전체를 조회하는 API입니다.")
    @GetMapping("/{artistId}")
    public BaseResponseDto getPortfolio(@PathVariable(name = "artistId") Long artistId,
                                    @RequestParam(value = "page", defaultValue = "0", required = false) int page
                                    ){
        return BaseResponseDto.SuccessResponse(SuccessStatus.PORTFOLIO_GET, portfolioService.getPortfolio(artistId, page));
    }

    @Operation(summary = "포트폴리오 조회", description = "특정 포트폴리오를 조회하는 API입니다.")
    @GetMapping("/details/{userId}/{portfolioId}")
    public BaseResponseDto getPortfolioDetails(@PathVariable(name = "userId") Long userId, @PathVariable(name = "portfolioId") Long portfolioId) {
        return BaseResponseDto.SuccessResponse(SuccessStatus.PORTFOLIO_GET, portfolioService.getPortfolioDetails(userId, portfolioId));
    }

    @Operation(summary = "포트폴리오 수정/삭제", description = "포트폴리오를 수정/삭제하는 API입니다.")
    @PatchMapping()
    public BaseResponseDto updatePortfolio(@RequestBody UpdatePortfolioDto portfolioDto){
        portfolioService.updatePortfolio(portfolioDto);
        return BaseResponseDto.SuccessResponse(SuccessStatus.PORTFOLIO_UPDATE);
    }

    /**recommend**/
    @Operation(summary = "포트폴리오 추천 - 리뷰 순", description = "리뷰가 많은 순으로 포트폴리오를 추천하는 API입니다.")
    @GetMapping("/recommend/review")
    public BaseResponseDto recommendReview(){
        return BaseResponseDto.SuccessResponse(SuccessStatus.RECOMMEND_REVIEW_GET, portfolioService.recommendReview());
    }

    @Operation(summary = "포트폴리오 추천 - 최신 순", description = "최근 등록된 순으로 포트폴리오를 추천하는 API입니다.")
    @GetMapping("/recommend/recent")
    public BaseResponseDto recommendRecent(){
        return BaseResponseDto.SuccessResponse(SuccessStatus.RECOMMEND_RECENT_GET, portfolioService.recommendRecent());
    }
}