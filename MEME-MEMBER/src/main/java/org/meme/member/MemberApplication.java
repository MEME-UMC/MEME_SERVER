package org.meme.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "org.meme.member.domain")
//@EntityScan(basePackages = "org.meme.member.domain")
@EnableJpaAuditing
public class MemeMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemeMemberApplication.class, args);
    }

}
