package com.hana.api.gpt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public OpenAiService(WebClient.Builder webClientBuilder, RedisTemplate<String, Object> redisTemplate) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
        this.redisTemplate = redisTemplate;
    }

    // Assistant 및 벡터 스토어 설정
    public void setupAssistantAndVectorStore() {
        // 벡터 스토어 ID 및 파일 ID 목록
        String vectorStoreId = "vs_Pojsve0wA3AZ2fRIR3dvpfJS";
        List<String> fileIds = List.of("file-Q0dEBVOgLlhyvvsN73t8xvgb");

        // 벡터 스토어에 파일 추가
        webClient.post()
                .uri("/beta/vector_stores/file_batches")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(Map.of("vector_store_id", vectorStoreId, "file_ids", fileIds))
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(response -> {
                    System.out.println("File Batch Status: " + response.get("status"));
                    System.out.println("File Counts: " + response.get("file_counts"));
                })
                .block();

        // Assistant 생성 및 벡터 스토어 추가
        webClient.post()
                .uri("/beta/assistants")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(Map.of(
                        "name", "HanaBankAssistant",
                        "instructions", "You provide information about Hana Bank's products based on the provided files.",
                        "model", "gpt-4o",
                        "tools", List.of(Map.of("type", "file_search", "vector_store_ids", List.of(vectorStoreId)))
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(response -> {
                    System.out.println("Assistant created with ID: " + response.get("id"));
                })
                .block();
    }

    // ChatGPT 대화 응답 메소드
    public Mono<Map<String, Object>> getChatResponse(String userCode, String prompt) {
        String cacheKey = "chat:" + userCode + ":" + prompt;
        // 캐시된 응답을 먼저 확인
        Map<String, Object> cachedResponse = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedResponse != null) {
            return Mono.just(cachedResponse);
        }

        String conversationCacheKey = "conversation:" + userCode;
        AtomicReference<List<Map<String, String>>> conversationRef = new AtomicReference<>(
                (List<Map<String, String>>) redisTemplate.opsForValue().get(conversationCacheKey)
        );

        if (conversationRef.get() == null) {
            conversationRef.set(new ArrayList<>());
        }

        // 이전 대화 추가
        List<Map<String, String>> messages = new ArrayList<>(conversationRef.get());
        messages.add(createMessage("user", prompt));

        return this.webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(new ChatRequest(messages))
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(response -> {
                    Map<String, String> assistantMessage = (Map<String, String>) ((Map<String, Object>) ((List<Object>) response.get("choices")).get(0)).get("message");
                    List<Map<String, String>> conversation = conversationRef.get();
                    conversation.add(createMessage("user", prompt));
                    conversation.add(assistantMessage);

                    // 대화 저장
                    redisTemplate.opsForValue().set(conversationCacheKey, conversation, Duration.ofMinutes(15));
                    // 응답 캐시 저장
                    redisTemplate.opsForValue().set(cacheKey, response, Duration.ofHours(1));
                })
                .map(response -> {
                    // 현재 대화와 이전 대화 합치기
                    List<Map<String, String>> combinedConversation = new ArrayList<>(conversationRef.get());
                    combinedConversation.add(createMessage("assistant", (String) response.get("content")));

                    Map<String, Object> result = new HashMap<>();
                    result.put("conversation", combinedConversation);
                    return result;
                });
    }

    // DALL-E 이미지 생성 메소드
    public Mono<Map> generateImage(String prompt) {
        String cacheKey = "dalle:" + prompt;
        Map<String, Object> cachedResponse = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedResponse != null) {
            return Mono.just(cachedResponse);
        }

        return this.webClient.post()
                .uri("/images/generations")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of("prompt", prompt, "size", "1024x1024"))
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(response -> redisTemplate.opsForValue().set(cacheKey, response, Duration.ofHours(1)));
    }

    private Map<String, String> createMessage(String role, String content) {
        Map<String, String> message = new HashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    private static class ChatRequest {
        private final String model = "gpt-4o";  // 모델 이름 변경
        private final List<Map<String, String>> messages;

        public ChatRequest(List<Map<String, String>> messages) {
            this.messages = messages;
        }

        public String getModel() {
            return model;
        }

        public List<Map<String, String>> getMessages() {
            return messages;
        }
    }
}
