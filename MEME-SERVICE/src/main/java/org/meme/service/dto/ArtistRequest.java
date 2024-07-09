package org.meme.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meme.domain.enums.*;

import java.util.List;

public class ArtistRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistProfileDto {
        private Long userId;
        private String profileImg;
        private String nickname;
        private Gender gender;
        private String introduction;
        private WorkExperience workExperience;
        private List<Region> region;
        private List<Category> specialization;
        private MakeupLocation makeupLocation;
        private String shopLocation;
    }
}
