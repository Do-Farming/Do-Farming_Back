package com.hana.api.group.service;

import com.hana.api.group.dto.GroupRequestDto;
import com.hana.api.group.dto.GroupResponseDto;
import com.hana.api.group.entity.Group;
import com.hana.api.group.repository.GroupRepository;
import com.hana.api.groupMember.entity.GroupMember;
import com.hana.api.groupMember.entity.GroupMemberPK;
import com.hana.api.groupMember.repository.GroupMemberRepository;
import com.hana.api.user.entity.User;
import com.hana.common.config.BaseException;
import com.hana.common.config.BaseResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.hana.api.user.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;


    public Group createGroup(GroupRequestDto.GroupCreateReq request, UUID userCode) {

        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));

        Group group = Group.builder()
                .groupName(request.getGroupName())
                .groupNumber(request.getGroupNumber())
                .title(request.getTitle())
                .description(request.getDesc())
                .wakeupTime(request.getWakeupTime())
                .isPublic(request.getIsPublic())
                .creatorName(user.getName())
                .status(0)
                .build();

        groupRepository.save(group);
        return group;
    }

    public Integer joinGroup(GroupRequestDto.GroupJoinReq request, UUID userCode) {
        try {
            User user = userRepository.findByUserCode(userCode)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));

            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.GROUPS_EMPTY_GROUP_ID));

            if (group.getStatus() == 0) {
                // 기본키 객체 생성
                GroupMemberPK groupMemberPK = new GroupMemberPK(request.getGroupId(), userCode);

                // 그룹 멤버 객체 생성
                GroupMember groupMember = GroupMember.builder()
                        .id(groupMemberPK)
                        .group(group)
                        .user(user)
                        .build();
                groupMemberRepository.save(groupMember);
                return 1;
            } else {
                return 0;
            }
        } catch (BaseException e) {
            // 예외 처리
            throw new BaseException(BaseResponseStatus.SYSTEM_ERROR);
        }
    }


    public List<GroupResponseDto.GetGroupListRes> groupList() {
        List<Group> groups = groupRepository.findAll();

        return groups.stream()
                .map(group -> {
                    int participantNumber = groupMemberRepository.countByGroupId(group.getId());
                    return GroupResponseDto.GetGroupListRes.from(group, participantNumber);
                })
                .collect(Collectors.toList());
    }
}
