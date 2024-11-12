package org.meme.service.domain.service;

import lombok.RequiredArgsConstructor;
import org.meme.service.common.status.ErrorStatus;
import org.meme.service.domain.entity.Model;
import org.meme.service.domain.repository.ArtistRepository;
import org.meme.service.domain.repository.FavoriteArtistRepository;
import org.meme.service.domain.repository.ModelRepository;
import org.meme.service.domain.converter.ArtistConverter;
import org.meme.service.domain.dto.response.ArtistResponse;
import org.springframework.stereotype.Service;
import org.meme.service.domain.entity.Artist;
import org.meme.service.common.exception.GeneralException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ModelRepository modelRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;


    //아티스트 프로필 조회 (Model Ver.)
    public ArtistResponse.ArtistDto getArtistProfile(Long userId, Long artistId){
        Model model = findModelById(userId);
        Artist artist = findArtistById(artistId);

        boolean isFavorite = favoriteArtistRepository.existsByModelAndArtist(model, artist);

        return ArtistConverter.toArtistDto(artist, isFavorite);
    }

    //아티스트 프로필 조회 (Artist Ver.)
    public ArtistResponse.ArtistDto getArtistProfileFromArtist(Long artistId){
        Artist artist = findArtistById(artistId);
        return ArtistConverter.toArtistDto(artist, true);
    }

    private Artist findArtistById(Long artistId){
        return artistRepository.findArtistByUserId(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
    }

    private Model findModelById(Long modelId){
        return modelRepository.findModelByUserId(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
    }
}
