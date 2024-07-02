package org.meme.auth.config.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "org.meme.domain")
@EnableJpaRepositories(basePackages = "org.meme.domain")
@ComponentScan("org.meme.domain")
public class JpaConfig {

}
