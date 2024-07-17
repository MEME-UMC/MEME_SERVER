package org.meme.service.converter;

import org.meme.domain.entity.*;
import org.meme.service.dto.response.ArtistResponse;
import org.meme.service.dto.response.FavoriteResponse;
import org.meme.service.dto.response.PortfolioResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class FavoriteConverter {
    // favoriteArtist
    public static FavoriteArtist toFavoriteArtist(Artist artist, Model model){
        return FavoriteArtist.builder()
                .model(model)
                .artistId(artist.getUserId())
                .build();
    }

    public static FavoriteResponse.FavoriteArtistPageDto toFavoriteArtistPageDto
            (Page<FavoriteArtist> page, List<ArtistResponse.ArtistSimpleDto> content){
        return FavoriteResponse.FavoriteArtistPageDto.builder()
                .content(content)
                .pageSize(page.getSize())
                .currentPage(page.getNumber())
                .totalNumber(page.getNumberOfElements())
                .totalPage(page.getTotalPages())
                .build();
    }

    // favoritePortfolio
    public static FavoritePortfolio toFavoritePortfolio(Model model, Portfolio portfolio){
        return FavoritePortfolio.builder()
                .model(model)
                .portfolio(portfolio)
                .build();
    }

    public static FavoriteResponse.FavoritePortfolioPageDto toFavoritePortfolioPageDto(Page<FavoritePortfolio> page){
        List<PortfolioResponse.PortfolioSimpleDto> content = page.stream()
                .map(PortfolioConverter::toPortfolioSimpleDto)
                .toList();

        return FavoriteResponse.FavoritePortfolioPageDto.builder()
                .content(content)
                .pageSize(page.getSize())
                .currentPage(page.getNumber())
                .totalNumber(page.getNumberOfElements())
                .totalPage(page.getTotalPages())
                .build();
    }
}
