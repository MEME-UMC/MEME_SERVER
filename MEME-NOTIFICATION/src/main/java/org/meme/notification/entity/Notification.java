package org.meme.notification.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class Notification {
    private Long userId;
    private String title;
    private String body;
    private List<String> token;
}
