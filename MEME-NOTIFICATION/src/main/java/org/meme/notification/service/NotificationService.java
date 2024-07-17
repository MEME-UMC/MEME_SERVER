package org.meme.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.meme.domain.dto.FcmSendDto;
import org.meme.notification.config.FcmKeyProperties;
import org.meme.notification.dto.FcmMessageDto;
import org.meme.notification.entity.NotificationDocument;
import org.meme.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FcmKeyProperties fcmKeyProperties;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "model_signup", groupId = "notification_service")
    public void consumeModelMessage(FcmSendDto fcmSendDto) throws IOException {
        System.out.println("Consumed message: " + fcmSendDto);
        System.out.println(sendMessageTo(fcmSendDto));
    }

    @KafkaListener(topics = "artist_signup", groupId = "notification_service")
    public void consumeArtistMessage(FcmSendDto fcmSendDto) {
        System.out.println("Consumed message: " + fcmSendDto);
    }

    public List<NotificationDocument> getNotificationsByUserId(Long userId) {
        List<NotificationDocument> byUserId = notificationRepository.findByUserId(userId);
        return byUserId;
    }

    public NotificationDocument createItem(NotificationDocument itemDocument) {
        return notificationRepository.save(itemDocument);
    }

    @Transactional
    public int sendMessageTo(FcmSendDto fcmSendDto) throws IOException {
        int successCount = 0;

        for (String token : fcmSendDto.getToken()) {

            notificationRepository.save(NotificationDocument.builder()
                    .userId(fcmSendDto.getUserId()).token(fcmSendDto.getToken())
                    .title(fcmSendDto.getTitle()).body(fcmSendDto.getBody())
                    .build());

            String message = makeMessage(fcmSendDto, token);
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + getAccessToken());

            HttpEntity<String> entity = new HttpEntity<>(message, headers);

            ResponseEntity<String> response = restTemplate.exchange(fcmKeyProperties.getUrl(), HttpMethod.POST, entity, String.class);

            System.out.println(response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                successCount++;
            }
        }

        return successCount;
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
     * @param token      개별 FCM 토큰
     * @return String
     */
    private String makeMessage(FcmSendDto fcmSendDto, String token) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(token)
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }
}