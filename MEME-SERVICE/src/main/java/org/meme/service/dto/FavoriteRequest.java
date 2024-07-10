package org.meme.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FavoriteRequest {

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteArtistDto {
        @NotBlank(message = "modelId를 입력해주세요.")
        private Long modelId;
        @NotBlank(message = "artistId를 입력해주세요.")
        private Long artistId;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoritePortfolioDto {
        @NotBlank(message = "modelId를 입력해주세요.")
        private Long modelId;
        @NotBlank(message = "portfolioId를 입력해주세요.")
        private Long portfolioId;
    }

}
