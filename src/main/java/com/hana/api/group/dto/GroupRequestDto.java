package com.hana.api.group.dto;

import com.hana.api.group.entity.Group;
import com.hana.api.user.entity.User;
import lombok.*;

public class GroupRequestDto {
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GroupCreateReq {
        private String groupName;
        private Integer groupNumber;
        private String title;
        private String desc;
        private String wakeupTime;
        private Boolean isPublic;
    }
}
