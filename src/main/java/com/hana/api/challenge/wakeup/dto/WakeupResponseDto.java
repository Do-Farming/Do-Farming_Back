package com.hana.api.challenge.wakeup.dto;

import lombok.*;

public class WakeupResponseDto {
    @Data
    public static class GetTodayObjectRes {
        private String object;
    }
}