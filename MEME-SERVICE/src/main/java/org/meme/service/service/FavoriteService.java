package org.meme.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.meme.domain.common.exception.GeneralException;
import org.meme.domain.common.status.ErrorStatus;
import org.meme.domain.entity.*;
import org.meme.domain.repository.*;
import org.meme.service.converter.ArtistConverter;
import org.meme.service.converter.FavoriteConverter;
import org.meme.service.dto.ArtistResponse;
import org.meme.service.dto.FavoriteRequest;
import org.meme.service.dto.FavoriteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final ModelRepository modelRepository;
    private final ArtistRepository artistRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;
    private final FavoritePortfolioRepository favoritePortfolioRepository;
    private final PortfolioRepository portfolioRepository;


    //관심 아티스트 조회
    @Transactional
    public FavoriteResponse.FavoriteArtistPageDto getFavoriteArtist(Long modelId, int page){
        Model model = modelRepository.findById(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));

        //paging
        List<FavoriteArtist> favoriteArtistList = model.getFavoriteArtistList();
        Page<FavoriteArtist> favoriteArtistPage = getPage(page, favoriteArtistList);

        //관심 아티스트 리스트
        List<ArtistResponse.ArtistSimpleDto> content = favoriteArtistPage.getContent().stream()
                .map(favoriteArtist -> {
                    Artist artist = artistRepository.findById(favoriteArtist.getArtistId())
                            .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
                    //해당 아티스트를 관심 아티스트로 설정한 모델 수 카운트
                    Long modelCount = favoriteArtistRepository.countByArtistId(artist.getUserId());
                    return ArtistConverter.toArtistSimpleDto(artist, modelCount);
                })
                .collect(Collectors.toList());

        return FavoriteConverter.toFavoriteArtistPageDto(favoriteArtistPage, content);
    }

    //관심 메이크업 조회
    @Transactional
    public FavoriteResponse.FavoritePortfolioPageDto getFavoritePortfolio(Long modelId, int page){
        Model model = modelRepository.findById(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));

        //list를 page로 변환
        List<FavoritePortfolio> favoritePortfolioList = model.getFavoritePortfolioList();
        Page<FavoritePortfolio> favoritePortfolioPage = getPage(page, favoritePortfolioList);

        return FavoriteConverter.toFavoritePortfolioPageDto(favoritePortfolioPage);
    }

    //관심 아티스트 추가
    @Transactional
    public void addFavoriteArtist(FavoriteRequest.FavoriteArtistDto favoriteArtistDto) {
        Model model = modelRepository.findById(favoriteArtistDto.getModelId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));

        Artist artist = artistRepository.findById(favoriteArtistDto.getArtistId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));

        //이미 관심 아티스트가 존재하는 경우
        if (favoriteArtistRepository.existsByModelAndArtistId(model, artist.getUserId())) {
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_FAVORITE_ARTIST);
        }

        FavoriteArtist favoriteArtist = FavoriteConverter.toFavoriteArtist(artist, model);
        model.updateFavoriteArtistList(favoriteArtist);
        favoriteArtistRepository.save(favoriteArtist);
    }

    //관심 메이크업 추가
    @Transactional
    public void addFavoritePortfolio(FavoriteRequest.FavoritePortfolioDto favoritePortfolioDto) {
        Model model = modelRepository.findById(favoritePortfolioDto.getModelId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));

        Portfolio portfolio = portfolioRepository.findById(favoritePortfolioDto.getPortfolioId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO));

        //이미 관심 포트폴리오가 존재하는 경우
        if (favoritePortfolioRepository.existsByModelAndPortfolio(model,portfolio)) {
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_FAVORITE_PORTFOLIO);
        }

        FavoritePortfolio favoritePortfolio = FavoriteConverter.toFavoritePortfolio(model, portfolio);
        model.updateFavoritePortfolioList(favoritePortfolio);
        favoritePortfolioRepository.save(favoritePortfolio);
    }

    //관심 아티스트 삭제
    @Transactional
    public void deleteFavoriteArtist(FavoriteRequest.FavoriteArtistDto favoriteArtistDto){
        Model model = modelRepository.findById(favoriteArtistDto.getModelId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));

        Artist artist = artistRepository.findById(favoriteArtistDto.getArtistId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));

        FavoriteArtist favoriteArtist = favoriteArtistRepository.findByModelAndArtistId(model, artist.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_FAVORITE_ARTIST));
        favoriteArtistRepository.delete(favoriteArtist);
    }

    //관심 메이크업 삭제
    @Transactional
    public void deleteFavoritePortfolio(FavoriteRequest.FavoritePortfolioDto favoritePortfolioDto) {
        Model model = modelRepository.findById(favoritePortfolioDto.getModelId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));

        Portfolio portfolio = portfolioRepository.findById(favoritePortfolioDto.getPortfolioId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO));

        FavoritePortfolio favoritePortfolio = favoritePortfolioRepository.findByModelAndPortfolio(model, portfolio)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_FAVORITE_PORTFOLIO));
        favoritePortfolioRepository.delete(favoritePortfolio);
    }

    private Page getPage(int page, List list){
        Pageable pageable = PageRequest.of(page, 30);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        //list를 page로 변환
        return new PageImpl<>(list.subList(start, end),
                pageable, list.size());
    }

}
