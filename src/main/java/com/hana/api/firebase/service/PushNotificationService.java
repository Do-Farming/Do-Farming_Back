package com.hana.api.firebase.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PushNotificationService {

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    public void sendNotificationToDevice(String token, String title, String message) {
        sendPushNotification(token, title, message);
    }
    //해당 service 이용해서 영어만!!! 넣어서 보내면 제대로 push 감 token은 user에서 deviedId 테이블 사용
    //title은 큰 글씨 message는 작은 글씨
    private void sendPushNotification(String token, String title, String message) {
        RestTemplate restTemplate = new RestTemplate();
        String payload = String.format("{\"to\":\"%s\",\"sound\":\"default\",\"title\":\"%s\",\"body\":\"%s\"}", token, title, message);
        restTemplate.postForEntity(EXPO_PUSH_URL, payload, String.class);
    }
}