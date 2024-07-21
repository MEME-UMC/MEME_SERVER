package org.meme.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meme.domain.dto.FcmSendDto;
import org.meme.notification.config.FcmKeyProperties;
import org.meme.notification.dto.FcmMessageDto;
import org.meme.notification.entity.NotificationDocument;
import org.meme.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FcmKeyProperties fcmKeyProperties;
    private final NotificationRepository notificationRepository;
    private final SlackService slackService;

    @Value("${slack.channel}")
    private String slackChannel;

    @Transactional
    public List<NotificationDocument> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Transactional
    public NotificationDocument createItem(NotificationDocument itemDocument) {
        return notificationRepository.save(itemDocument);
    }

    @Transactional
    public boolean sendMessageTo(FcmSendDto fcmSendDto) throws IOException {
        int successCount = 0;

        for (String token : fcmSendDto.getToken()) {
            notificationRepository.save(NotificationDocument.builder()
                    .userId(fcmSendDto.getUserId())
                    .token(fcmSendDto.getToken())
                    .title(fcmSendDto.getTitle())
                    .body(fcmSendDto.getBody())
                    .build());

            String message = makeMessage(fcmSendDto, token);
            // 테스트
            // slackService.sendSlackMessage(message, slackChannel);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + getAccessToken());

            HttpEntity<String> entity = new HttpEntity<>(message, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fcmKeyProperties.getUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            log.info("Response Status Code: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                successCount++;
            }
        }

        return successCount == fcmSendDto.getToken().size();
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = fcmKeyProperties.getPath();

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of(fcmKeyProperties.getScope()));

        googleCredentials.refreshIfExpired();
        String token = googleCredentials.getAccessToken().getTokenValue();

        log.info("Access Token: {}", token);

        return token;
    }

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