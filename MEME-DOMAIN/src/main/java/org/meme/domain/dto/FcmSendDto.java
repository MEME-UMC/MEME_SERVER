package org.meme.domain.dto;

import lombok.*;

import java.util.List;
/**
 * 모바일에서 전달받은 객체
 *
 * @author : lee
 * @fileName : FcmSendDto
 * @since : 2/21/24
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmSendDto {
    private List<String> token;

    private String title;

    private String body;

    @Builder(toBuilder = true)
    public FcmSendDto(List<String> token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
