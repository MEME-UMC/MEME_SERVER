package org.meme.notification;

import lombok.extern.slf4j.Slf4j;
import org.meme.domain.common.BaseResponseDto;
import org.meme.domain.common.status.SuccessStatus;
import org.meme.domain.dto.FcmSendDto;
import org.meme.notification.entity.NotificationDocument;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j(topic = "MEME-NOTIFICATION")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService fcmService) {
        this.notificationService = fcmService;
    }

    @GetMapping("/api/notification")
    public BaseResponseDto elasticsearchSearchTest(@RequestParam Long userId) {
        log.debug("[+] ELK 아이템 조회 ");
        return BaseResponseDto.SuccessResponse(SuccessStatus.ELK_SEARCH_SUCCESS, notificationService.getNotificationsByUserId(userId));
    }

    @PostMapping("/api/notification")
    public BaseResponseDto elasticsearchCreateTest(@RequestBody NotificationDocument itemDocument) {
        log.debug("[+] ELK 새롭게 아이템 생성 ");
        return BaseResponseDto.SuccessResponse(SuccessStatus.ELK_CREATE_SUCCESS, notificationService.createItem(itemDocument));
    }

    @PostMapping("/api/fcm/v1/send")
    public BaseResponseDto pushMessageTest(@RequestBody @Validated FcmSendDto fcmSendDto) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다. ");
        return BaseResponseDto.SuccessResponse(SuccessStatus.FCM_SEND_SUCCESS, notificationService.sendMessageTo(fcmSendDto));
    }

    @KafkaListener(topics = "model_signup", groupId = "notification_service")
    public void consumeModelMessage(FcmSendDto fcmSendDto) throws IOException {
        log.info("Consumed model message: {}", fcmSendDto);
        notificationService.sendMessageTo(fcmSendDto);
    }

    @KafkaListener(topics = "artist_signup", groupId = "notification_service")
    public void consumeArtistMessage(FcmSendDto fcmSendDto) throws IOException{
        log.info("Consumed artist message: {}", fcmSendDto);
        notificationService.sendMessageTo(fcmSendDto);
    }
}
