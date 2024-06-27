package com.hana.api.group.service;

import com.hana.api.group.dto.GroupRequestDto;
import com.hana.api.group.entity.Group;
import com.hana.api.group.repository.GroupRepository;
import com.hana.api.user.entity.User;
import com.hana.common.config.BaseException;
import com.hana.common.config.BaseResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.hana.api.user.repository.UserRepository;

import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;


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
                .build();

        groupRepository.save(group);
        return group;
    }

}
