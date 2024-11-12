package org.meme.service.domain.converter;

import org.meme.service.domain.entity.Artist;
import org.meme.service.domain.dto.response.ArtistResponse;
import org.meme.service.domain.dto.response.PortfolioResponse;

import java.util.List;

public class ArtistConverter {
     public static ArtistResponse.ArtistDto toArtistDto(Artist artist, boolean isFavorite){

        List<PortfolioResponse.PortfolioSimpleDto> portfolioDtoList = artist.getPortfolioList()
                .stream()
                .map(PortfolioConverter::toPortfolioSimpleDto)
                .toList();

        return ArtistResponse.ArtistDto.builder()
                .artistId(artist.getUserId())
                .isFavorite(isFavorite)
                .gender(artist.getUser().getGender())
                .nickname(artist.getUser().getNickname())
                .profileImg(artist.getUser().getProfileImg())
                .email(artist.getUser().getEmail())
                .phoneNumber(artist.getPhoneNumber())
                .instagramId(artist.getInstagramId())
                .introduction(artist.getIntroduction())
                .workExperience(artist.getWorkExperience())
                .region(artist.getRegion())
                .specialization(artist.getSpecialization())
                .makeupLocation(artist.getMakeupLocation())
                .simplePortfolioDtoList(portfolioDtoList)
                .build();
    }

    public static ArtistResponse.ArtistSimpleDto toArtistSimpleDto(Artist artist, Long modelCount) {
         String region = artist.getRegion().isEmpty() ? null : artist.getRegion().get(0).getValue();

        return ArtistResponse.ArtistSimpleDto.builder()
                .artistId(artist.getUserId())
                .profileImg(artist.getUser().getProfileImg())
                .artistNickName(artist.getUser().getNickname())
                .email(artist.getUser().getEmail())
                .region(region)
                .modelCount(modelCount)
                .build();
    }

}
