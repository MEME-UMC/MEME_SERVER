package org.meme.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ComponentScan("org.meme.domain")
@ComponentScan("org.meme.auth")
@SpringBootApplication
@EnableJpaAuditing
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class,args);
    }
}