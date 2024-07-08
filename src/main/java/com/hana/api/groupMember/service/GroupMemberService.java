package com.hana.api.groupMember.service;

import com.hana.api.groupMember.repository.GroupMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    public Long getGroupIdByUserCode(UUID userCode) {
        return groupMemberRepository.findGroupIdByUserCode(userCode);
    }

}
