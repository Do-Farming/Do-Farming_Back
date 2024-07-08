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

    // 그룹의 만보기 데이터를 가져오는 메서드
    public List<PedometerResponse> getGroupPedometer(Long groupId) {
        List<WalkChallenge> walkChallenges = walkChallengeRepository.findByWalkDateAndGroupId(LocalDate.now(), groupId);
        if (walkChallenges.isEmpty()) {
            throw new IllegalArgumentException("No walk challenges found for the given group and date");
        }

        // 각 사용자의 최신 WalkChallenge를 맵으로 변환
        Map<UUID, WalkChallenge> latestWalkChallenges = walkChallenges.stream()
                .collect(Collectors.toMap(
                        wc -> wc.getUser().getUserCode(),
                        wc -> wc,
                        (wc1, wc2) -> wc1.getWalkDate().isAfter(wc2.getWalkDate()) ? wc1 : wc2
                ));

        // 최신 WalkChallenge 데이터를 기반으로 PedometerResponse 리스트 생성
        return latestWalkChallenges.values().stream()
                .map(wc -> {
                    List<DailyRank> latestRanks = dailyRankRepository.findTop1ByUserOrderByDailyDateDesc(wc.getUser().getUserCode());
                    if (latestRanks.isEmpty()) {
                        throw new IllegalArgumentException("No rank found for user with userCode: " + wc.getUser().getUserCode());
                    }
                    DailyRank latestRank = latestRanks.get(0);
                    return new PedometerResponse(
                            wc.getUser().getName(),
                            wc.getStep(),
                            latestRank.getTotalRate()
                    );
                })
                .collect(Collectors.toList());
    }

    // WalkChallenge 데이터를 저장하는 메서드
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

        // 사용자 별로 최신 DailyRank를 가져옵니다.
        List<DailyRank> latestRanks = dailyRankRepository.findTop1ByUserOrderByDailyDateDesc(userCode);
        if (latestRanks.isEmpty()) {
            throw new IllegalArgumentException("No rank found for user with userCode: " + userCode);
        }
        DailyRank latestRank = latestRanks.get(0);
        PedometerResponse response = new PedometerResponse(user.getName(), steps, latestRank.getTotalRate());

        try {
            pedometerWebSocketHandler.broadcastUpdatedSteps(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
