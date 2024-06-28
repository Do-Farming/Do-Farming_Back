package com.hana.api.group.dto;

import com.hana.api.group.entity.Group;
import lombok.*;

import java.time.LocalDateTime;

public class GroupResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetGroupListRes {
        private long id;
        private String groupName;
        private Integer groupNumber;
        private Integer participantNumber;
        private String title;
        private LocalDateTime createdDate;

        public static GetGroupListRes from(Group group, int participantNumber) {
            return GetGroupListRes.builder()
                    .id(group.getId())
                    .groupName(group.getGroupName())
                    .groupNumber(group.getGroupNumber())
                    .participantNumber(participantNumber)
                    .title(group.getTitle())
                    .createdDate(group.getCreatedDate())
                    .build();
        }

    }
}
