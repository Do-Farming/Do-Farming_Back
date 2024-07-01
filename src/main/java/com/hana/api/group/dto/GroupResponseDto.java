package com.hana.api.group.dto;

import com.hana.api.group.entity.Group;
import com.hana.api.groupMember.entity.GroupMember;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetGroupInfoRes {
        private long id;
        private String groupName;
        private Integer groupNumber;
        private Integer participantNumber;
        private String title;
        private String description;
        private String wakeupTime;
        private LocalDateTime createdDate;
        private boolean isPublic;
        private boolean isAdmin;
        private Integer status;
        private boolean isJoined;
        private List<GroupMemberDto> groupMembers;

        public static GetGroupInfoRes from(Group group, int participantNumber, boolean isAdmin, boolean isJoined, List<GroupMember> groupMembers) {
            return GetGroupInfoRes.builder()
                    .id(group.getId())
                    .groupName(group.getGroupName())
                    .groupNumber(group.getGroupNumber())
                    .participantNumber(participantNumber)
                    .title(group.getTitle())
                    .description(group.getDescription())
                    .wakeupTime(group.getWakeupTime())
                    .createdDate(group.getCreatedDate())
                    .isPublic(group.getIsPublic())
                    .isAdmin(isAdmin)
                    .status(group.getStatus())
                    .isJoined(isJoined)
                    .groupMembers(groupMembers.stream().map(GroupMemberDto::from).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GroupMemberDto {
        private long id;
        private String memberName;

        public static GroupMemberDto from(GroupMember groupMember) {
            return GroupMemberDto.builder()
                    .id(groupMember.getGroup().getId())
                    .memberName(groupMember.getUser().getName())
                    .build();
        }
    }
}
