package org.meme.service.domain.service;

import lombok.RequiredArgsConstructor;
import org.meme.service.domain.dto.request.PortfolioRequest;
import org.meme.service.domain.dto.response.PortfolioResponse;
import org.meme.service.domain.entity.*;
import org.meme.service.domain.converter.PortfolioConverter;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.meme.service.domain.repository.ArtistRepository;
import org.meme.service.domain.repository.FavoritePortfolioRepository;
import org.meme.service.domain.repository.ModelRepository;
import org.meme.service.domain.repository.PortfolioImgRepository;
import org.meme.service.domain.repository.PortfolioRepository;
import org.meme.service.common.status.ErrorStatus;
import org.meme.service.common.exception.GeneralException;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {
    private final ArtistRepository artistRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioImgRepository portfolioImgRepository;
    private final ModelRepository modelRepository;
    private final FavoritePortfolioRepository favoritePortfolioRepository;
    private final int pageSize = 30;

    //포트폴리오 생성
    @Transactional
    public Long createPortfolio(PortfolioRequest.CreatePortfolioDto portfolioDto) {
        Artist artist = findArtistById(portfolioDto.getArtistId());

        //포트폴리오 이름이 이미 존재할 시
        validDuplicatePortfolioName(portfolioDto);

        // 포트폴리오 이미지 리스트 생성
        List<PortfolioImg> portfolioImgList = portfolioDto.getPortfolioImgSrc().stream()
                .map(PortfolioConverter::toPortfolioImg)
                .toList();

        // 포트폴리오 entity 생성
        Portfolio portfolio = PortfolioConverter.toPortfolio(artist, portfolioDto);

        // 포트폴리오 이미지, 포트폴리오 연관관계 설정
        portfolioImgList.forEach(portfolio::addPortfolioImg);

        // 포트폴리오 연관관계 설정
        artist.updatePortfolioList(portfolio);
        portfolioRepository.save(portfolio);
        return portfolio.getPortfolioId();
    }

    // 포트폴리오 전체 조회
    @Transactional
    public PortfolioResponse.PortfolioPageDto getPortfolio(Long artistId, int page) {
        Artist artist = findArtistById(artistId);

        // 아티스트의 전체 포트폴리오 리스트 조회
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Portfolio> portfolioList = portfolioRepository.findPortfoliosByArtist(artist, pageable);

        //검색 결과가 없을 시
        if(portfolioList.getContent().isEmpty())
            throw new GeneralException(ErrorStatus.SEARCH_NOT_FOUNT);

        return PortfolioConverter.toPortfolioPageDto(portfolioList);
    }

    // 포트폴리오 하나만 조회
    public PortfolioResponse.PortfolioDetailDto getPortfolioDetails(Long userId, Long portfolioId) {
        Model model = findModelById(userId);
        Portfolio portfolio = findPortfolioById(portfolioId);
        boolean isFavorite = favoritePortfolioRepository.existsByModelAndPortfolio(model, portfolio);

        return PortfolioConverter.toPortfolioDetailDto(portfolio, isFavorite);
    }

    // 포트폴리오 수정/삭제
    @Transactional
    public void updatePortfolio(PortfolioRequest.UpdatePortfolioDto updatePortfolioDto) {
        Artist artist = findArtistById(updatePortfolioDto.getArtistId());
        Portfolio portfolio = findPortfolioById(updatePortfolioDto.getPortfolioId());

        validPortfolioIsBlock(portfolio, updatePortfolioDto);
        validArtistAuthorizedForPortfolio(portfolio, artist);

        // 포트폴리오 이미지 수정
        if (!updatePortfolioDto.getPortfolioImgSrcList().isEmpty())
            updatePortfolioImgList(portfolio, updatePortfolioDto.getPortfolioImgSrcList());

        // 포트폴리오 수정
        updatePortfolioEntity(portfolio, updatePortfolioDto);
    }

    public void updatePortfolioImgList(Portfolio portfolio, List<String> portfolioImgDtoList) {
        List<PortfolioImg> updatedPortfolioImgList = new ArrayList<>();

        for (String portfolioImgSrc : portfolioImgDtoList) {
            if (portfolioImgSrc == null)
                throw new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO_IMG);

            Optional<PortfolioImg> portfolioImg = portfolioImgRepository.findBySrcAndPortfolio(portfolioImgSrc, portfolio);
            if (portfolioImg.isEmpty()) {
                // 새로운 이미지 추가
                PortfolioImg newPortfolioImg = PortfolioConverter.toPortfolioImg(portfolioImgSrc);
                newPortfolioImg.setPortfolio(portfolio);
                portfolioImgRepository.save(newPortfolioImg);
                updatedPortfolioImgList.add(newPortfolioImg);
            } else {
                // 기존 이미지 보존
                updatedPortfolioImgList.add(portfolioImg.get());
            }
        }

        // 기존 리뷰 이미지 리스트와 새로운 리뷰 이미지 리스트 비교
        List<PortfolioImg> existedPortfolioImgList = portfolio.getPortfolioImgList();
        for (PortfolioImg portfolioImg : existedPortfolioImgList) {
            if (!updatedPortfolioImgList.contains(portfolioImg)) {
                portfolioImgRepository.delete(portfolioImg);
            }
        }

        portfolio.updatePortfolioImgList(updatedPortfolioImgList);
    }

    /**
     * recommend
     **/
    //리뷰 많은 순 포트폴리오 추천
    public List<PortfolioResponse.PortfolioSimpleDto> recommendReview() {
        List<Portfolio> portfolioList = portfolioRepository.findPortfolioByReviewList();

        return portfolioList.stream()
                .map(PortfolioConverter::toPortfolioSimpleDto)
                .toList();
    }

    //최신 등록 순 포트폴리오 추천
    public List<PortfolioResponse.PortfolioSimpleDto> recommendRecent() {
        List<Portfolio> portfolioList = portfolioRepository.findPortfolioByCreatedAt();

        return portfolioList.stream()
                .map(PortfolioConverter::toPortfolioSimpleDto)
                .toList();
    }

    // 포트폴리오 블락 상태 변경
    @Transactional
    public void blockPortfolio(Long userId, Long portfolioId){
        Artist artist = findArtistById(userId);
        Portfolio portfolio = findPortfolioById(portfolioId);

        validArtistAuthorizedForPortfolio(portfolio, artist);

        portfolio.updateBlock(!portfolio.isBlock());
    }

    private void validArtistAuthorizedForPortfolio(Portfolio portfolio, Artist artist) {
        if (portfolio.getArtist() != artist)
            throw new GeneralException(ErrorStatus.NOT_AUTHORIZED_PORTFOLIO);
    }

    private void validPortfolioIsBlock(Portfolio portfolio, PortfolioRequest.UpdatePortfolioDto requestDto) {
        if (portfolio.isBlock() && requestDto.getIsBlock())
            throw new GeneralException(ErrorStatus.BLOCKED_PORTFOLIO);
    }

    private void validDuplicatePortfolioName(PortfolioRequest.CreatePortfolioDto portfolioDto) {
        if (portfolioRepository.existsByMakeupName(portfolioDto.getMakeupName()))
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_PORTFOLIO);
    }

    private Artist findArtistById(Long artistId){
        return artistRepository.findArtistByUserId(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
    }

    private Model findModelById(Long modelId){
        return modelRepository.findModelByUserId(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
    }

    private Portfolio findPortfolioById(Long portfolioId){
        return portfolioRepository.findPortfolioById(portfolioId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO));
    }

    private void updatePortfolioEntity(Portfolio portfolio, PortfolioRequest.UpdatePortfolioDto request) {
        portfolio.updateCategory(request.getCategory());
        portfolio.updatePrice(request.getPrice());
        portfolio.updateInfo(request.getInfo());
        portfolio.updateMakeupName(request.getMakeupName());
        portfolio.updateBlock(request.getIsBlock());
    }
}