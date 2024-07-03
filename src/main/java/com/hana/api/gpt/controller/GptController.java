package com.hana.api.gpt.controller;

import com.hana.api.auth.Auth;
import com.hana.api.gpt.service.OpenAiService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    @Autowired
    private OpenAiService openAiService;

    @GetMapping("/chat")
    public Mono<Map<String, Object>> chat(@Parameter(hidden = true) @Auth String userCode, @RequestParam String prompt) {
        return openAiService.getChatResponse(userCode, prompt);
    }

    @PostMapping("/generate-image")
    public Mono<Map> generateImage(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        return openAiService.generateImage(prompt);
    }
}
