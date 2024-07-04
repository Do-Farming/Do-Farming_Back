package com.hana.api.challenge.wakeup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

public class WakeupResponseDto {
    @Data
    public static class GetTodayObjectRes {
        private String object;
    }

    @Data
    public static class WakeupCertificateDto {
        private String userId;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        private LocalDateTime wakeupTime;
    }
}