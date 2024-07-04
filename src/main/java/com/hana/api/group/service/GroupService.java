package com.hana.api.group.service;

import com.hana.api.dailyRank.entity.DailyRank;
import com.hana.api.dailyRank.repository.DailyRankRepository;
import com.hana.api.group.dto.GroupRequestDto;
import com.hana.api.group.dto.GroupResponseDto;
import com.hana.api.group.entity.Group;
import com.hana.api.group.repository.GroupRepository;
import com.hana.api.groupMember.entity.GroupMember;
import com.hana.api.groupMember.entity.GroupMemberPK;
import com.hana.api.groupMember.repository.GroupMemberRepository;
import com.hana.api.user.entity.User;
import com.hana.api.weeklyRate.entity.WeeklyRate;
import com.hana.api.weeklyRate.repository.WeeklyRateRepository;
import com.hana.common.config.BaseException;
import com.hana.common.config.BaseResponseStatus;
import com.hana.common.scheduler.DynamicSchedulerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.hana.api.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private final WeeklyRateRepository weeklyRateRepository;
    private final DailyRankRepository dailyRankRepository;
    private final DynamicSchedulerService dynamicSchedulerService;

    @Transactional
    public void insertWeeklyRateForGroupMembers() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Group> groups = groupRepository.findActiveGroup(tomorrow);

        for (Group group : groups) {
            LocalDate start = LocalDate.from(group.getStartedAt());
            int weekly = (((int) ChronoUnit.DAYS.between(start, tomorrow)) / 7) + 1;
            for (GroupMember member : group.getGroupMembers()) {
                WeeklyRate weeklyRate = WeeklyRate.builder()
                                                    .user(member.getUser())
                                                    .weekly(weekly).rate(3.5).build();
                DailyRank dailyRank = DailyRank.builder()
                                        .user(member.getUser())
                        .group(member.getGroup())
                        .dailyRate(0)
                        .dailyDate(tomorrow)
                        .totalRate(3.5)
                        .dailyRank(0).build();
                weeklyRateRepository.save(weeklyRate);
                dailyRankRepository.save(dailyRank);
            }
        }
    }
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

        GroupMemberPK groupMemberPK = new GroupMemberPK(group.getId(), userCode);
        GroupMember groupMember = GroupMember.builder()
                .id(groupMemberPK)
                .group(group)
                .user(user)
                .build();
        groupMemberRepository.save(groupMember);
        return group;
    }

    // 사용자가 이미 그룹에 가입되어 있는지 확인
    public boolean isUserAlreadyInAnyGroup(UUID userCode) {
        List<GroupMember> userGroups = groupMemberRepository.findByUserUserCode(userCode);
        return !userGroups.isEmpty(); // 사용자가 이미 그룹에 가입되어 있으면 true 반환
    }

    public Integer joinGroup(GroupRequestDto.GroupJoinReq request, UUID userCode) {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));

        // 사용자가 도파밍 상품에 가입되어 있는지 확인
        if (isUserAlreadyInAnyGroup(userCode)) {
            return 3;
        }

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.GROUPS_EMPTY_GROUP_ID));

        if (group.getStatus() != 0) {
            return 0; // 그룹이 모집 중
        }

        boolean isAlreadyMember = groupMemberRepository.isAlreadyJoin(request.getGroupId(), userCode);
        if (isAlreadyMember) {
            return 2; // 이미 해당 그룹에 가입된 경우
        }

        // 중복 가입이 없으면 그룹 멤버 추가
        GroupMemberPK groupMemberPK = new GroupMemberPK(request.getGroupId(), userCode);
        GroupMember groupMember = GroupMember.builder()
                .id(groupMemberPK)
                .group(group)
                .user(user)
                .build();
        groupMemberRepository.save(groupMember);

        int participantNumber = groupMemberRepository.countByGroupId(group.getId());

        if (group.getGroupNumber() == participantNumber) {
            group.setStatus(1);
            groupRepository.save(group);

            // 차주 월요일에 status를 2로 바꾸는 스케줄링
            dynamicSchedulerService.scheduleStatusUpdate(group.getId());
        }
        return 1; // 성공적으로 그룹에 가입


    }

    public List<GroupResponseDto.GetGroupListRes> groupList() {
        List<Group> groups = groupRepository.findByIsPublicTrue();

        return groups.stream()
                .map(group -> {
                    int participantNumber = groupMemberRepository.countByGroupId(group.getId());
                    return GroupResponseDto.GetGroupListRes.from(group, participantNumber);
                })
                .collect(Collectors.toList());
    }

    public GroupResponseDto.GetGroupInfoRes groupDetail(long groupId, UUID userCode) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.GROUPS_EMPTY_GROUP_ID));

        int participantNumber = groupMemberRepository.countByGroupId(group.getId());
        boolean isJoined = groupMemberRepository.isAlreadyJoin(groupId, userCode);

        // 사용자가 그룹의 생성자인지 여부 확인
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));
        boolean isAdmin = group.getCreatorName().equals(user.getName());

        return GroupResponseDto.GetGroupInfoRes.from(group, participantNumber, isAdmin, isJoined, group.getGroupMembers());
    }

    public void groupDelete(long groupId, UUID userCode) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new BaseException(BaseResponseStatus.GROUPS_EMPTY_GROUP_ID));
        User user = userRepository.findByUserCode(userCode).orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID));

        if (user.getName().equals(group.getCreatorName())) {
            groupRepository.deleteById(groupId);
        } else {
            throw new BaseException(BaseResponseStatus.GROUPS_UNAUTHORIZED);
        }
    }

    public List<User> getUsersByGroupId(Long groupId) {
        return groupRepository.findUsersByGroupId(groupId);
    }
}
