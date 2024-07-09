package org.meme.notification;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class NotificationService {
    @KafkaListener(topics = "model_signup", groupId = "notification_service")
    public void consumeMessage(String message) {
        System.out.println("Consumed message: " + message);
    }
}

