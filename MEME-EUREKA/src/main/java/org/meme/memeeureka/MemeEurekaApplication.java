package org.meme.memeeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MemeEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemeEurekaApplication.class, args);
    }

}
