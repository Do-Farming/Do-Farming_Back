package com.hana.api.gpt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public OpenAiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    public Mono<String> getChatResponse(String prompt) {
        return this.webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(new ChatRequest(prompt))
                .retrieve()
                .bodyToMono(String.class);
    }

    private static class ChatRequest {
        private final String model = "gpt-3.5-turbo";
        private final List<Map<String, String>> messages;

        public ChatRequest(String prompt) {
            messages = new ArrayList<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);
        }

        public String getModel() {
            return model;
        }

        public List<Map<String, String>> getMessages() {
            return messages;
        }
    }
}
