package org.meme.memeconfig.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitConfig {

    @Value("${GIT_PRIVATE_KEY}")
    private String gitPrivateKey;

    @Bean
    public String formattedGitPrivateKey() {
        return gitPrivateKey.replace("\\n", "\n");
    }
}
