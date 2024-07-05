package com.hana.common.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // 서비스 계정 키 파일 경로
        String serviceAccountPath = "src/main/resources/firebase-service-account.json";
// 서비스 계정 키 파일을 읽기 위한 InputStream
        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

        // FirebaseOptions를 사용하여 FirebaseApp 초기화
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // FirebaseApp이 이미 초기화되어 있는지 확인하고, 초기화되지 않은 경우에만 초기화
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}