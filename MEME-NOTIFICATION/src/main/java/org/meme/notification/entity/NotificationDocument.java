package org.meme.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "notifications")
public class NotificationDocument {
    @Id
    private Long userId;
    private List<String> token;
    private String title;
    private String body;

    public static NotificationDocument from(Notification notification) {
        return NotificationDocument.builder()
                .userId(notification.getUserId())
                .token(notification.getToken())
                .title(notification.getTitle())
                .body(notification.getBody())
                .build();
    }
}