package com.aminoacids.youtubenotes.controllers;

import com.aminoacids.youtubenotes.dao.BotRequest;
import com.aminoacids.youtubenotes.dao.BotSummaryResponse;
import com.aminoacids.youtubenotes.services.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openai")
@RequiredArgsConstructor
public class OpenAiController {
    private final OpenAiService openAiService;

    @PostMapping("/create")
    public ResponseEntity<BotSummaryResponse> createSummary(@RequestBody BotRequest botRequest) {
        return ResponseEntity.ok(openAiService.createSummary(botRequest));
    }
}
