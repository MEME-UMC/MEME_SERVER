package org.meme.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.service.domain.enums.Category;
import org.meme.service.domain.enums.MakeupLocation;
import org.meme.service.domain.enums.Region;

import java.util.List;


public class PortfolioResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioDto {
        private Long portfolioId;
        private Category category;
        private String artistNickName;
        private Long userId;
        private String makeupName;
        private int price;
        private MakeupLocation makeupLocation; //샵 재직 여부
        private String shopLocation; //샵 위치
        private List<Region> region; //활동 가능 지역
        private Boolean isBlock;
        private String averageStars;
        private int reviewCount; //리뷰 개수
        private List<PortfolioImgDto> portfolioImgDtoList;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioDetailDto{
        private Long portfolioId;
        private Long userId;
        private Boolean isFavorite;
        private Category category;
        private String artistProfileImg;
        private String artistNickName;
        private String makeupName;
        private int price;
        private String info;
        private MakeupLocation makeupLocation; //샵 재직 여부
        private String shopLocation; //샵 위치
        private List<Region> region; //활동 가능 지역
        private Boolean isBlock;
        private String averageStars;
        private int reviewCount; //리뷰 개수
        private String durationTime; //소요 시간
        private List<PortfolioImgDto> portfolioImgDtoList;
    }


    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioImgDto {
        private Long portfolioImgId;
        private String portfolioImgSrc;
    }



    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioPageDto {
        private List<PortfolioDto> content;
        private int currentPage; //현재 페이지 번호
        private int pageSize; //페이지 크기
        private int totalNumber; //전체 메이크업 개수
        private int totalPage; //전체 페이지 개수
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioSimpleDto {
        private Long portfolioId;
        private String portfolioImg;
        private Category category;
        private String makeupName;
        private String artistName;
        private String artistEmail;
        private int price;
        private MakeupLocation makeupLocation; //샵 재직 여부
        private String averageStars; //별점
    }


}
