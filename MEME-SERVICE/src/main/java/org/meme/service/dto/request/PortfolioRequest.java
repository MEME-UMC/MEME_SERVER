package org.meme.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.Category;

import java.util.List;

public class PortfolioRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePortfolioDto {
        @NotBlank(message = "artistId를 입력해주세요.")
        private Long artistId;
        @NotBlank(message = "카테고리를 입력해주세요")
        private Category category;
        @NotBlank(message = "메이크업 명을 입력해주세요")
        private String makeupName;
        @NotBlank(message = "가격을 입력해주세요")
        private int price;
        @NotBlank(message = "메이크업 정보를 입력해주세요")
        private String info;
        @NotBlank(message = "메이크업 소요시간을 입력해주세요")
        private String durationTime;
        @NotBlank(message = "포트폴리오 이미지를 업로드해주세요")
        private List<String> portfolioImgSrc;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePortfolioDto{
        @NotBlank(message = "artistId를 입력해주세요.")
        private Long artistId;
        @NotBlank(message = "portfolioId를 입력해주세요.")
        private Long portfolioId;
        private Category category;
        private String makeupName;
        private int price;
        private String info;
        private Boolean isBlock;
        private List<String> portfolioImgSrcList;

    }
}
