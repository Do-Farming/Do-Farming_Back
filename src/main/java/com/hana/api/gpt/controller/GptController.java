package com.hana.api.gpt.controller;

import com.hana.api.gpt.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    @Autowired
    private OpenAiService openAiService;

    @GetMapping("/chat")
    public Mono<String> chat(@RequestParam String prompt) {
        return openAiService.getChatResponse(prompt);
    }
}
