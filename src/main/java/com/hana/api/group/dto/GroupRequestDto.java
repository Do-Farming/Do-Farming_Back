package com.hana.api.group.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

public class GroupRequestDto {
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GroupCreateReq {
        @NotEmpty
        private String groupName;
        @NotEmpty
        private Integer groupNumber;
        @NotEmpty
        private String title;
        @NotEmpty
        private String desc;
        @NotEmpty
        private String wakeupTime;
        @NotEmpty
        private Boolean isPublic;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GroupJoinReq {
        private Long groupId;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GroupDeleteReq {
        private int groupId;
    }

}
