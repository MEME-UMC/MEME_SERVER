package org.meme.notification;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SlackService {
    @Value("${slack.token}")
    private String token;

    public void sendSlackMessage(String message, String channel) {
        try {
            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message)
                    .build();

            ChatPostMessageResponse chatPostMessageResponse = methods.chatPostMessage(request);
            log.info("Slack - Test Message 전송 완료 : {}", chatPostMessageResponse);
        } catch (Exception e) {
            log.warn("Slack Error - {}", e.getMessage());
        }
    }
}