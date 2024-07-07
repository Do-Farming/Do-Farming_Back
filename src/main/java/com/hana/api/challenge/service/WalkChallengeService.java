package com.hana.api.challenge.service;

import com.hana.api.challenge.dto.response.PedometerResponse;
import com.hana.api.challenge.entity.WalkChallenge;
import com.hana.api.challenge.repository.WalkChallengeRepository;
import com.hana.api.challenge.websocket.PedometerWebSocketHandler;
import com.hana.api.dailyRank.entity.DailyRank;
import com.hana.api.dailyRank.repository.DailyRankRepository;
import com.hana.api.group.entity.Group;
import com.hana.api.group.repository.GroupRepository;
import com.hana.api.groupMember.repository.GroupMemberRepository;
import com.hana.api.user.entity.User;
import com.hana.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalkChallengeService {

    private final WalkChallengeRepository walkChallengeRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final DailyRankRepository dailyRankRepository;
    private final PedometerWebSocketHandler pedometerWebSocketHandler;

    public List<PedometerResponse> getGroupPedometer(Long groupId) {
        List<WalkChallenge> walkChallenges = walkChallengeRepository.findByWalkDateAndGroupId(LocalDate.now(), groupId);
        if (walkChallenges.isEmpty()) {
            throw new IllegalArgumentException("No walk challenges found for the given group and date");
        }

        Map<UUID, WalkChallenge> latestWalkChallenges = walkChallenges.stream()
                .collect(Collectors.toMap(
                        wc -> wc.getUser().getUserCode(),
                        wc -> wc,
                        (wc1, wc2) -> wc1.getWalkDate().isAfter(wc2.getWalkDate()) ? wc1 : wc2
                ));

        return latestWalkChallenges.values().stream()
                .map(wc -> {
                    DailyRank latestRank = dailyRankRepository.findLatestByUser(wc.getUser().getUserCode())
                            .orElseThrow(() -> new IllegalArgumentException("No rank found for user with userCode: " + wc.getUser().getUserCode()));
                    return new PedometerResponse(
                            wc.getUser().getName(),
                            wc.getStep(),
                            latestRank.getTotalRate()
                    );
                })
                .collect(Collectors.toList());
    }

    public void saveWalkChallenge(UUID userCode, int steps) {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user code: " + userCode));
        Long groupId = groupMemberRepository.findGroupByUserId(user.getPhoneNumber());

        if (groupId == null) {
            throw new IllegalArgumentException("User with phone number " + user.getPhoneNumber() + " is not part of any group");
        }

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + groupId));

        WalkChallenge walkChallenge = WalkChallenge.builder()
                .step(steps)
                .walkDate(LocalDate.now())
                .user(user)
                .group(group)
                .build();

        walkChallengeRepository.save(walkChallenge);

        // Create a PedometerResponse and broadcast the update
        DailyRank latestRank = dailyRankRepository.findLatestByUser(userCode)
                .orElseThrow(() -> new IllegalArgumentException("No rank found for user with userCode: " + userCode));
        PedometerResponse response = new PedometerResponse(user.getName(), steps, latestRank.getTotalRate());

        try {
            pedometerWebSocketHandler.broadcastUpdatedSteps(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
