package org.meme.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.meme.domain.dto.FcmSendDto;
import org.meme.notification.dto.ApiResponseWrapper;
import org.meme.notification.dto.SuccessCode;
import org.meme.notification.entity.NotificationDocument;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService fcmService) {
        this.notificationService = fcmService;
    }

    @GetMapping("/api/notification")
    public ResponseEntity<ApiResponseWrapper<Object>> elasticsearchSearchTest(@RequestBody Long userId) {
        ApiResponseWrapper<Object> arw = ApiResponseWrapper
                .builder()
                .result(notificationService.getNotificationsByUserId(userId))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(arw, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/api/notification")
    public ResponseEntity<ApiResponseWrapper<Object>> elasticsearchCreateTest(@RequestBody NotificationDocument itemDocument) {
        ApiResponseWrapper<Object> arw = ApiResponseWrapper
                .builder()
                .result(notificationService.createItem(itemDocument))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(arw, HttpStatusCode.valueOf(200));
    }


    @PostMapping("/api/fcm/v1/send")
    public ResponseEntity<ApiResponseWrapper<Object>> pushMessageTest(@RequestBody @Validated FcmSendDto fcmSendDto) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다. ");
        int result = notificationService.sendMessageTo(fcmSendDto);

        ApiResponseWrapper<Object> arw = ApiResponseWrapper
                .builder()
                .result(result)
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(arw, HttpStatusCode.valueOf(200));
    }
}
