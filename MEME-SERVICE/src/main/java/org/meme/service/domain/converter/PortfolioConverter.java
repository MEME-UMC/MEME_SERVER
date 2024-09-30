package org.meme.service.domain.converter;

import org.meme.service.domain.entity.Artist;
import org.meme.service.domain.entity.FavoritePortfolio;
import org.meme.service.domain.entity.Portfolio;
import org.meme.service.domain.entity.PortfolioImg;
import org.meme.service.domain.dto.request.PortfolioRequest;
import org.meme.service.domain.dto.response.PortfolioResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PortfolioConverter {
    public static Portfolio toPortfolio(Artist artist, PortfolioRequest.CreatePortfolioDto dto) {
        return Portfolio.builder()
                .artist(artist)
                .category(dto.getCategory())
                .makeupName(dto.getMakeupName())
                .info(dto.getInfo())
                .price(dto.getPrice())
                .portfolioImgList(new ArrayList<PortfolioImg>())
                .averageStars("0.00")
                .durationTime(dto.getDurationTime())
                .isBlock(false)
                .build();
    }

    public static PortfolioResponse.PortfolioDto toPortfolioDto(Portfolio portfolio){
        Artist artist = portfolio.getArtist();

        // PortfolioImg 리스트를 PortfolioImgDto 리스트로 변환
        List<PortfolioResponse.PortfolioImgDto> portfolioImgDtoList = portfolio.getPortfolioImgList()
                .stream()
                .map(PortfolioConverter::toPortfolioImgDto)
                .toList();

        return PortfolioResponse.PortfolioDto.builder()
                .portfolioId(portfolio.getPortfolioId())
                .userId(portfolio.getArtist().getUserId())
                .category(portfolio.getCategory())
                .artistNickName(artist.getNickname())
                .makeupName(portfolio.getMakeupName())
                .price(portfolio.getPrice())
                .makeupLocation(artist.getMakeupLocation())
                .shopLocation(artist.getShopLocation())
                .region(artist.getRegion())
                .isBlock(portfolio.isBlock())
                .portfolioImgDtoList(portfolioImgDtoList)
                .averageStars(portfolio.getAverageStars())
                .reviewCount(portfolio.getReviewList().size())
                .build();
    }

    public static PortfolioResponse.PortfolioDetailDto toPortfolioDetailDto(Portfolio portfolio, boolean isFavorite) {
            Artist artist = portfolio.getArtist();

            // PortfolioImg 리스트를 PortfolioImgDto 리스트로 변환
            List<PortfolioResponse.PortfolioImgDto> portfolioImgDtoList = portfolio.getPortfolioImgList()
                    .stream()
                    .map(PortfolioConverter::toPortfolioImgDto)
                    .toList();

            return PortfolioResponse.PortfolioDetailDto.builder()
                    .portfolioId(portfolio.getPortfolioId())
                    .userId(portfolio.getArtist().getUserId())
                    .isFavorite(isFavorite)
                    .category(portfolio.getCategory())
                    .artistProfileImg(artist.getProfileImg())
                    .artistNickName(artist.getNickname())
                    .makeupName(portfolio.getMakeupName())
                    .price(portfolio.getPrice())
                    .info(portfolio.getInfo())
                    .makeupLocation(artist.getMakeupLocation())
                    .shopLocation(artist.getShopLocation())
                    .region(artist.getRegion())
                    .isBlock(portfolio.isBlock())
                    .portfolioImgDtoList(portfolioImgDtoList)
                    .averageStars(portfolio.getAverageStars())
                    .reviewCount(portfolio.getReviewList().size())
                    .durationTime(portfolio.getDurationTime())
                    .build();
    }

    public static PortfolioImg toPortfolioImg(String src) {
        return PortfolioImg.builder()
                .src(src)
                .build();
    }

    public static PortfolioResponse.PortfolioImgDto toPortfolioImgDto(PortfolioImg img){
        return PortfolioResponse.PortfolioImgDto.builder()
                .portfolioImgId(img.getPortfolioImgId())
                .portfolioImgSrc(img.getSrc())
                .build();
    }

    public static PortfolioResponse.PortfolioPageDto toPortfolioPageDto(Page<Portfolio> page){
        List<PortfolioResponse.PortfolioDto> content = page.stream()
                .map(PortfolioConverter::toPortfolioDto)
                .toList();

        return PortfolioResponse.PortfolioPageDto.builder()
                .content(content)
                .pageSize(page.getSize())
                .currentPage(page.getNumber())
                .totalNumber(page.getNumberOfElements())
                .totalPage(page.getTotalPages())
                .build();
    }

    // PortfolioSimpleDto
    public static PortfolioResponse.PortfolioSimpleDto toPortfolioSimpleDto(Portfolio portfolio){
        Artist artist = portfolio.getArtist();

        return PortfolioResponse.PortfolioSimpleDto.builder()
                .portfolioId(portfolio.getPortfolioId())
                .portfolioImg(portfolio.getPortfolioImgList().get(0).getSrc())
                .category(portfolio.getCategory())
                .makeupName(portfolio.getMakeupName())
                .artistName(artist.getNickname())
                .artistEmail(artist.getEmail())
                .price(portfolio.getPrice())
                .makeupLocation(artist.getMakeupLocation())
                .averageStars(portfolio.getAverageStars())
                .build();
    }

    public static PortfolioResponse.PortfolioSimpleDto toPortfolioSimpleDto(FavoritePortfolio favoritePortfolio){
        Portfolio portfolio = favoritePortfolio.getPortfolio();
        Artist artist = portfolio.getArtist();

        return PortfolioResponse.PortfolioSimpleDto.builder()
                .portfolioId(portfolio.getPortfolioId())
                .portfolioImg(portfolio.getPortfolioImgList().get(0).getSrc())
                .category(portfolio.getCategory())
                .makeupName(portfolio.getMakeupName())
                .artistName(artist.getNickname())
                .artistEmail(artist.getEmail())
                .price(portfolio.getPrice())
                .makeupLocation(artist.getMakeupLocation())
                .averageStars(portfolio.getAverageStars())
                .build();
    }
}
