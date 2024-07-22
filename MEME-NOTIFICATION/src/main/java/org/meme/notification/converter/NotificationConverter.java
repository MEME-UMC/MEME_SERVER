package org.meme.notification.converter;

import org.meme.domain.dto.FcmSendDto;
import org.meme.notification.entity.NotificationDocument;

public class NotificationConverter {
    public static NotificationDocument toDocument(FcmSendDto fcmSendDto) {
        return NotificationDocument.builder()
                .userId(fcmSendDto.getUserId())
                .token(fcmSendDto.getToken())
                .title(fcmSendDto.getTitle())
                .body(fcmSendDto.getBody())
                .build();
    }
}
