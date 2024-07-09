package org.meme.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.status.ErrorStatus;
import org.meme.domain.entity.FavoriteArtist;
import org.meme.domain.entity.Model;
import org.meme.domain.repository.ArtistRepository;
import org.meme.domain.repository.FavoriteArtistRepository;
import org.meme.domain.repository.ModelRepository;
import org.meme.service.converter.ArtistConverter;
import org.meme.service.dto.ArtistRequest;
import org.meme.service.dto.ArtistResponse;
import org.springframework.stereotype.Service;
import org.meme.domain.entity.Artist;
import org.meme.domain.common.exception.GeneralException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ModelRepository modelRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;


    //아티스트 프로필 관리/수정
    @Transactional
    public void updateArtistProfile(ArtistRequest.ArtistProfileDto profileDto){
        Artist artist = artistRepository.findById(profileDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
        artist.updateArtist(profileDto);
    }

    //아티스트 프로필 조회 (관리 조회 용)
    public ArtistResponse.ArtistProfileDto getProfile(Long userId){
        Artist artist = artistRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
        return ArtistConverter.toArtistProfileDto(artist);
    }

    //아티스트 프로필 조회 (Model Ver.)
    public ArtistResponse.ArtistDto getArtistProfile(Long userId, Long artistId){
        Model model = modelRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));

        boolean isFavorite = false;
        Optional<FavoriteArtist> favoriteArtist = favoriteArtistRepository.findByModelAndArtistId(model, artistId);
        if(favoriteArtist.isPresent())
            isFavorite = true;

        return ArtistConverter.toArtistDto(artist, isFavorite);
    }

    //아티스트 예약 가능 시간 편집
    // TODO: AvailableTime
    @Transactional
    public void patchArtistAvailableTime() {
//        Artist artist = artistRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new GlobalException(ErrorStatus.NOT_EXIST_ARTIST));
//
//        //새로운 예약 시간 설정
//        List<AvailableTime> availableTimeList = dto.getAvailableTimeDtoList().stream()
//                .map(AvailableTime::from)
//                .peek(availableTime -> availableTime.updateArtist(artist))
//                .toList();
//        artist.updateAvailableTimeList(availableTimeList);
    }

    //아티스트 프로필 조회 (Artist Ver.)
    public ArtistResponse.ArtistDto getArtistProfileFromArtist(Long artistId){
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
        return ArtistConverter.toArtistDto(artist, true);
    }
}
