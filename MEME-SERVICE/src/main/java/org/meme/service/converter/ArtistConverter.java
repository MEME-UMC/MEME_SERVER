package org.meme.service.converter;

import org.meme.domain.entity.Artist;
import org.meme.service.dto.response.ArtistResponse;
import org.meme.service.dto.response.PortfolioResponse;

import java.util.List;

public class ArtistConverter {
     public static ArtistResponse.ArtistDto toArtistDto(Artist artist, boolean isFavorite){
       // TODO: AvailableTime
//        List<AvailableTimeDto> availableTimeDtoList = artist.getAvailableTimeList()
//                .stream().map(AvailableTimeDto::from)
//                .toList();

        List<PortfolioResponse.PortfolioSimpleDto> portfolioDtoList = artist.getPortfolioList()
                .stream()
                .map(PortfolioConverter::toPortfolioSimpleDto)
                .toList();

        return ArtistResponse.ArtistDto.builder()
                .artistId(artist.getUserId())
                .isFavorite(isFavorite)
                .gender(artist.getGender())
                .nickname(artist.getNickname())
                .profileImg(artist.getProfileImg())
                .email(artist.getEmail())
                .introduction(artist.getIntroduction())
                .workExperience(artist.getWorkExperience())
                .region(artist.getRegion())
                .specialization(artist.getSpecialization())
                .makeupLocation(artist.getMakeupLocation())
//                .availableTimeList(availableTimeDtoList)
                .simplePortfolioDtoList(portfolioDtoList)
                .build();
    }

    public static ArtistResponse.ArtistSimpleDto toArtistSimpleDto(Artist artist, Long modelCount) {
        return ArtistResponse.ArtistSimpleDto.builder()
                .artistId(artist.getUserId())
                .profileImg(artist.getProfileImg())
                .artistNickName(artist.getNickname())
                .email(artist.getEmail())
                .region(artist.getRegion().get(0).getValue())
                .modelCount(modelCount)
                .build();
    }

}
