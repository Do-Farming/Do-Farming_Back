package com.hana.api.gpt.controller;

import com.hana.api.auth.Auth;
import com.hana.api.gpt.service.OpenAiService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    @Autowired
    private OpenAiService openAiService;

    @GetMapping("/chat")
    public Mono<Map<String, Object>> chat(@RequestParam String prompt,@Parameter(hidden = true) @Auth String userCode) {
        return openAiService.getChatResponse(prompt, userCode);
    }
}
