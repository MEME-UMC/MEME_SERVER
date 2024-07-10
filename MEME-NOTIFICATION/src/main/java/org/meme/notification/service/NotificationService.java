package org.meme.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.transaction.Transactional;
import org.meme.notification.config.FcmKeyProperties;
import org.meme.notification.dto.FcmMessageDto;
import org.meme.notification.dto.FcmSendDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class NotificationService {

    private final FcmKeyProperties fcmKeyProperties;

    @Autowired
    public NotificationService(FcmKeyProperties fcmKeyProperties) {
        this.fcmKeyProperties = fcmKeyProperties;
    }

    @KafkaListener(topics = "model_signup", groupId = "notification_service")
    public void consumeMessage(String message) {
        System.out.println("Consumed message: " + message);
    }

    @Transactional
    public int sendMessageTo(FcmSendDto fcmSendDto) throws IOException {

        String message = makeMessage(fcmSendDto);
        RestTemplate restTemplate = new RestTemplate();
        /**
         * 추가된 사항 : RestTemplate 이용중 클라이언트의 한글 깨짐 증상에 대한 수정
         * @reference : https://stackoverflow.com/questions/29392422/how-can-i-tell-resttemplate-to-post-with-utf-8-encoding
         */
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        ResponseEntity<String> response = restTemplate.exchange(fcmKeyProperties.getUrl(), HttpMethod.POST, entity, String.class);

        System.out.println(response.getStatusCode());

        return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
    }

    /**
     * Firebase Admin SDK의 비공개 키를 참조하여 Bearer 토큰을 발급 받습니다.
     *
     * @return Bearer token
     */
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = fcmKeyProperties.getPath();

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of(fcmKeyProperties.getScope()));

        googleCredentials.refreshIfExpired();
        String token = googleCredentials.getAccessToken().getTokenValue();

        // 토큰 확인을 위한 로그 추가
        System.out.println("Access Token: " + token);

        return token;
    }

    /**
     * FCM 전송 정보를 기반으로 메시지를 구성합니다. (Object -> String)
     *
     * @param fcmSendDto FcmSendDto
     * @return String
     */
    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(fcmSendDto.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }
}