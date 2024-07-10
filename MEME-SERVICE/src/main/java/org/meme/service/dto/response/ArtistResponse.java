package org.meme.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.*;

import java.util.List;

public class ArtistResponse {

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistDto {
        private Long artistId; //관심 등록 여부
        private Boolean isFavorite;
        private Gender gender;
        private String nickname;
        private String profileImg;
        private String email;
        private String introduction;
        private WorkExperience workExperience;
        private String shopLocation; //샵 위치
        private List<Region> region;
        private List<Category> specialization;
        private MakeupLocation makeupLocation;
//        private List<AvailableTimeDto> availableTimeList;
        private List<PortfolioResponse.PortfolioSimpleDto> simplePortfolioDtoList;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistPageDto {
        private List<ArtistDto> content;
        private int currentPage; //현재 페이지 번호
        private int pageSize; //페이지 크기
        private int totalNumber; //전체 메이크업 개수
        private int totalPage; //전체 페이지 개수
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistSimpleDto {
        private Long artistId;
        private String profileImg;
        private String artistNickName;
        private String email;
        private String region;
        private Long modelCount; //해당 아티스트를 관심 아티스트로 설정한 모델 수
    }
}
