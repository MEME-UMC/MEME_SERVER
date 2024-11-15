package org.meme.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class FavoriteResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteArtistPageDto {
        private List<ArtistResponse.ArtistSimpleDto> content;
        private int currentPage; //현재 페이지 번호
        private int pageSize; //페이지 크기
        private int totalNumber; //전체 메이크업 개수
        private int totalPage; //전체 페이지 개수
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoritePortfolioPageDto {
        private List<PortfolioResponse.PortfolioSimpleDto> content;
        private int currentPage; //현재 페이지 번호
        private int pageSize; //페이지 크기
        private int totalNumber; //전체 메이크업 개수
        private int totalPage; //전체 페이지 개수
    }

}
