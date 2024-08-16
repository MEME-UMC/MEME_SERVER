package org.meme.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.meme.reservation.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final TestService testService;

    @GetMapping("/api/v2/dungdung/test")
    public String test() {
        testService.readyForService();
        return "hi!";
    }
}
