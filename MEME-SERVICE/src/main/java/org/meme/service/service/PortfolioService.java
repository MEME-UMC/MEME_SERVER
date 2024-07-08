package org.meme.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.meme.service.converter.PortfolioConverter;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.meme.domain.entity.Artist;
import org.meme.domain.repository.ArtistRepository;
import org.meme.domain.entity.FavoritePortfolio;
import org.meme.domain.repository.FavoritePortfolioRepository;
import org.meme.domain.entity.Model;
import org.meme.domain.repository.ModelRepository;
import org.meme.service.dto.PortfolioRequest.*;
import org.meme.service.dto.PortfolioResponse.*;
import org.meme.domain.entity.Portfolio;
import org.meme.domain.entity.PortfolioImg;
import org.meme.domain.repository.PortfolioImgRepository;
import org.meme.domain.repository.PortfolioRepository;
import org.meme.domain.common.status.ErrorStatus;
import org.meme.domain.common.exception.GeneralException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final ArtistRepository artistRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioImgRepository portfolioImgRepository;
    private final ModelRepository modelRepository;
    private final FavoritePortfolioRepository favoritePortfolioRepository;

    //포트폴리오 생성
    @Transactional
    public Long createPortfolio(CreatePortfolioDto portfolioDto) {
        Artist artist = artistRepository.findById(portfolioDto.getArtistId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));

        //포트폴리오 이름이 이미 존재할 시
        if (portfolioRepository.existsByMakeupName(portfolioDto.getMakeupName()))
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_PORTFOLIO);

        // 포트폴리오 이미지 리스트 생성
        List<PortfolioImg> portfolioImgList = portfolioDto.getPortfolioImgSrc().stream()
                .map(PortfolioImg::from)
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
    public PortfolioPageDto getPortfolio(Long artistId, int page) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));

        List<Portfolio> portfolioList = artist.getPortfolioList();

        //isblock이면 리스트에서 제거
        portfolioList.removeIf(Portfolio::isBlock);

        //list를 page로 변환
        Page<Portfolio> portfolioPage = getPage(page, portfolioList);

        return PortfolioConverter.toPortfolioPageDto(portfolioPage);
    }

    // 포트폴리오 하나만 조회
    public PortfolioDetailDto getPortfolioDetails(Long userId, Long portfolioId) {
        Model model = modelRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO));

        if (portfolio.isBlock())
            throw new GeneralException(ErrorStatus.BLOCKED_PORTFOLIO);

        boolean isFavorite = false;
        Optional<FavoritePortfolio> favoritePortfolio = favoritePortfolioRepository.findByModelAndPortfolio(model, portfolio);
        if (favoritePortfolio.isPresent())
            isFavorite = true;

        return PortfolioConverter.toPortfolioDetailDto(portfolio, isFavorite);
    }

    // 포트폴리오 수정/삭제
    @Transactional
    public void updatePortfolio(UpdatePortfolioDto updatePortfolioDto) {
        Artist artist = artistRepository.findById(updatePortfolioDto.getArtistId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));

        Portfolio portfolio = portfolioRepository.findById(updatePortfolioDto.getPortfolioId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO));

        if (portfolio.isBlock() && updatePortfolioDto.getIsBlock())
            throw new GeneralException(ErrorStatus.BLOCKED_PORTFOLIO);

        if (!portfolio.getArtist().equals(artist))
            throw new GeneralException(ErrorStatus.NOT_AUTHORIZED_PORTFOLIO);

        // 포트폴리오 이미지 수정
        if (!updatePortfolioDto.getPortfolioImgSrcList().isEmpty())
            updatePortfolioImgList(portfolio, updatePortfolioDto.getPortfolioImgSrcList());

        // 포트폴리오 수정
        // TODO:
//        portfolio.updatePortfolio(updatePortfolioDto);
    }

    @Transactional
    public void updatePortfolioImgList(Portfolio portfolio, List<String> portfolioImgDtoList) {
        List<PortfolioImg> updatedPortfolioImgList = new ArrayList<>();

        for (String portfolioImgSrc : portfolioImgDtoList) {
            if (portfolioImgSrc == null)
                throw new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO_IMG);

            Optional<PortfolioImg> portfolioImg = portfolioImgRepository.findBySrcAndPortfolio(portfolioImgSrc, portfolio);
            if (portfolioImg.isEmpty()) {
                // 새로운 이미지 추가
                PortfolioImg newPortfolioImg = PortfolioImg.from(portfolioImgSrc);
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
                // 이미지 삭제
                portfolioImgRepository.delete(portfolioImg);
            }
        }

        // 포트폴리오 이미지 리스트 - 포트폴리오 연관관계 설정
        portfolio.updatePortfolioImgList(updatedPortfolioImgList);
    }

    /**
     * recommend
     **/
    //리뷰 많은 순 포트폴리오 추천
    public List<PortfolioSimpleDto> recommendReview() {
        Pageable pageable = setPageRequest(0, "review");
        Page<Portfolio> portfolioList = portfolioRepository.findAllNotBlocked(pageable);

        return portfolioList.getContent().stream()
                .map(PortfolioConverter::toPortfolioSimpleDto)
                .toList();
    }

    //최신 등록 순 포트폴리오 추천
    public List<PortfolioSimpleDto> recommendRecent() {
        Pageable pageable = setPageRequest(0, "recent");
        Page<Portfolio> portfolioList = portfolioRepository.findAllNotBlocked(pageable);

        return portfolioList.getContent().stream()
                .map(PortfolioConverter::toPortfolioSimpleDto)
                .toList();
    }

    private Pageable setPageRequest(int page, String sortBy) {

        Sort sort = switch (sortBy) {
            case "desc" -> Sort.by("price").descending();
            case "asc" -> Sort.by("price").ascending();
            case "review" -> Sort.by("averageStars").descending();
            case "recent" -> Sort.by("createdAt").descending();
            default -> throw new GeneralException(ErrorStatus.INVALID_SORT_CRITERIA);
        };

        //별점 높은 순 정렬 추가
        Sort finalSort = sort.and(Sort.by("averageStars").descending());
        return PageRequest.of(page, 30, finalSort);
    }

    private Page<Portfolio> getPage(int page, List<Portfolio> list) {
        Pageable pageable = PageRequest.of(page, 30);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        //list를 page로 변환
        return new PageImpl<>(list.subList(start, end),
                pageable, list.size());
    }


}