package org.meme.service.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.TimeoutException;
import org.meme.service.common.exception.GeneralException;
import org.meme.service.common.status.ErrorStatus;
import org.meme.service.domain.entity.*;
import org.meme.service.domain.repository.*;
import org.meme.service.domain.converter.ArtistConverter;
import org.meme.service.domain.converter.FavoriteConverter;
import org.meme.service.domain.dto.response.ArtistResponse;
import org.meme.service.domain.dto.request.FavoriteRequest;
import org.meme.service.domain.dto.response.FavoriteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j // Lombok을 사용하여 Logger 생성
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final ModelRepository modelRepository;
    private final ArtistRepository artistRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;
    private final FavoritePortfolioRepository favoritePortfolioRepository;
    private final PortfolioRepository portfolioRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 인스턴스 재사용
    // CompletableFuture로 응답을 비동기 대기
    private CompletableFuture<String> responseFuture = new CompletableFuture<>();

    //관심 아티스트 조회
    @Transactional
    public FavoriteResponse.FavoriteArtistPageDto getFavoriteArtist(Long modelId, int page){
        Model model = findModelById(modelId);

        //paging
        List<FavoriteArtist> favoriteArtistList = model.getFavoriteArtistList();
        Page<FavoriteArtist> favoriteArtistPage = getPage(page, favoriteArtistList);

        //관심 아티스트 리스트
        List<ArtistResponse.ArtistSimpleDto> content = favoriteArtistPage.getContent().stream()
                .map(favoriteArtist -> {
                    Artist artist = favoriteArtist.getArtist();
                    //해당 아티스트를 관심 아티스트로 설정한 모델 수 카운트
                    Long modelCount = favoriteArtistRepository.countByArtist(artist);
                    return ArtistConverter.toArtistSimpleDto(artist, modelCount);
                })
                .collect(Collectors.toList());

        return FavoriteConverter.toFavoriteArtistPageDto(favoriteArtistPage, content);
    }

    //관심 메이크업 조회
    @Transactional
    public FavoriteResponse.FavoritePortfolioPageDto getFavoritePortfolio(Long modelId, int page){
        Model model = findModelById(modelId);

        //list를 page로 변환
        List<FavoritePortfolio> favoritePortfolioList = model.getFavoritePortfolioList();
        Page<FavoritePortfolio> favoritePortfolioPage = getPage(page, favoritePortfolioList);

        return FavoriteConverter.toFavoritePortfolioPageDto(favoritePortfolioPage);
    }

    //관심 아티스트 추가
    @Transactional
    public void addFavoriteArtist(FavoriteRequest.FavoriteArtistDto favoriteArtistDto) {
        Model model = findModelById(favoriteArtistDto.getModelId());
        Artist artist = findArtistById(favoriteArtistDto.getArtistId());

        //이미 관심 아티스트가 존재하는 경우
        if (favoriteArtistRepository.existsByModelAndArtist(model, artist)) {
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_FAVORITE_ARTIST);
        }

        FavoriteArtist favoriteArtist = FavoriteConverter.toFavoriteArtist(artist, model);
        model.updateFavoriteArtistList(favoriteArtist);
        favoriteArtistRepository.save(favoriteArtist);
    }

    // 관심 메이크업 추가: Kafka 메시지 전송 및 응답 대기
    @Transactional
    public String addFavoritePortfolioV2(FavoriteRequest.FavoritePortfolioDto favoritePortfolioDto) {
        try {
            // DTO를 JSON 문자열로 직렬화
            String message = objectMapper.writeValueAsString(favoritePortfolioDto);

            // Kafka에 메시지 전송
            kafkaTemplate.send("FavoritePortfolioRequest", message).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send message to Kafka", ex);
                } else {
                    log.info("Message sent to Kafka: {}", message);
                }
            });

            log.info("Waiting for response from Kafka...");

            // Kafka 응답 대기 (최대 10초)
            String response = responseFuture.get(10, TimeUnit.SECONDS);
            log.info("Received response: " + response);

            // response에서 PortfolioId 추출
            Portfolio portfolio = objectMapper.readValue(message, Portfolio.class);
            log.info("PortfolioId: " + portfolio.getPortfolioId());

            return response;

        } catch (JsonProcessingException e) {
            log.error("JSON serialization failed", e);
            throw new RuntimeException("Failed to serialize request", e);
        } catch (TimeoutException e) {
            log.error("Kafka response timed out", e);
            throw new RuntimeException("Kafka response timed out", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            throw new RuntimeException("Unexpected error", e);
        }
    }

    // Kafka Listener: FavoritePortfolioResponse 토픽에서 응답 수신
    @KafkaListener(topics = "FavoritePortfolioResponse", groupId = "favorite-consumer-group")
    public void listenResponse(String message) {
        log.info("Received response from Kafka: " + message);
        // CompletableFuture에 응답 전달
        responseFuture.complete(message);
        // 새 요청 처리를 위해 CompletableFuture 초기화
        responseFuture = new CompletableFuture<>();
    }

    //관심 메이크업 추가
    @Transactional
    public void addFavoritePortfolio(FavoriteRequest.FavoritePortfolioDto favoritePortfolioDto) {
        Model model = findModelById(favoritePortfolioDto.getModelId());
        Portfolio portfolio = findPortfolioById(favoritePortfolioDto.getPortfolioId());

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
        Model model = findModelById(favoriteArtistDto.getModelId());
        Artist artist = findArtistById(favoriteArtistDto.getArtistId());

        FavoriteArtist favoriteArtist = findFavoriteArtistByModelAndArtist(model, artist);
        favoriteArtistRepository.delete(favoriteArtist);
    }

    //관심 메이크업 삭제
    @Transactional
    public void deleteFavoritePortfolio(FavoriteRequest.FavoritePortfolioDto favoritePortfolioDto) {
        Model model = findModelById(favoritePortfolioDto.getModelId());
        Portfolio portfolio = findPortfolioById(favoritePortfolioDto.getPortfolioId());

        FavoritePortfolio favoritePortfolio = findFavoritePortfolioByModelAndPortfolio(model, portfolio);
        favoritePortfolioRepository.delete(favoritePortfolio);
    }

    private Artist findArtistById(Long artistId){
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ARTIST));
    }

    private Model findModelById(Long modelId){
        return modelRepository.findById(modelId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_MODEL));
    }

    private Portfolio findPortfolioById(Long portfolioId){
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PORTFOLIO));
    }

    private FavoritePortfolio findFavoritePortfolioByModelAndPortfolio(Model model, Portfolio portfolio){
        return favoritePortfolioRepository.findByModelAndPortfolio(model, portfolio)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_FAVORITE_PORTFOLIO));
    }

    private FavoriteArtist findFavoriteArtistByModelAndArtist(Model model, Artist artist){
        return favoriteArtistRepository.findByModelAndArtist(model, artist)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_FAVORITE_ARTIST));
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
