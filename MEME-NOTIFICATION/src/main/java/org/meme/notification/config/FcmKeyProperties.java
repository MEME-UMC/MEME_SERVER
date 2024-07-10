package org.meme.notification.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "fcm.key")
public class FcmKeyProperties {
    // Getters and Setters
    private String url;
    private String scope;
    private String path;
}
