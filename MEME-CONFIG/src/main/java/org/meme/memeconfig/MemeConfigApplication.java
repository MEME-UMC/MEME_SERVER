package org.meme.memeconfig;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class MemeConfigApplication {

	@Value("${GIT_PRIVATE_KEY}")
	private String gitPrivateKey;

	public static void main(String[] args) {
		SpringApplication.run(MemeConfigApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.setProperty("spring.cloud.config.server.git.private-key", gitPrivateKey.replace("\\n", "\n"));
	}

}
