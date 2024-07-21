package org.meme.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SlackController {
    private final SlackService slackService;

    @GetMapping("/slack/test")
    public void slackMessageTest() {
        slackService.sendSlackMessage("테스트 메시지 전송", "#server-logs");
        log.info("Slack Test");
    }
}
