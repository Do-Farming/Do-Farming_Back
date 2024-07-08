package com.hana.api.dailyRank.service;

import com.hana.api.challenge.entity.ChallengeRecord;
import com.hana.api.challenge.repository.ChallengeRecordRepository;
import com.hana.api.challenge.wakeup.entity.WakeupChallenge;
import com.hana.api.challenge.wakeup.repository.WakeupChallengeRepository;
import com.hana.api.dailyRank.dto.RankDto;
import com.hana.api.dailyRank.dto.Response.DailyRankResponse;
import com.hana.api.dailyRank.entity.DailyRank;
import com.hana.api.dailyRank.repository.DailyRankRepository;
import com.hana.api.challenge.entity.WalkChallenge;
import com.hana.api.challenge.entity.QuizChallenge;
import com.hana.api.challenge.repository.WalkChallengeRepository;
import com.hana.api.challenge.repository.QuizChallengeRepository;
import com.hana.api.group.repository.GroupRepository;
import com.hana.api.user.entity.User;
import com.hana.common.config.BaseException;
import com.hana.common.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyRankService {
    private final DailyRankRepository dailyRankRepository;
    private final WalkChallengeRepository walkChallengeRepository;
    private final WakeupChallengeRepository wakeupChallengeRepository;
    private final QuizChallengeRepository quizChallengeRepository;
    private final GroupRepository groupRepository;
    private final ChallengeRecordRepository challengeRecordRepository;

    @Transactional
    public void calculateDailyRanks() {
        LocalDate now = LocalDate.now();
        ChallengeRecord challengeRecord = challengeRecordRepository.findByChallengeDate(now);
        int challenge = challengeRecord.getChallengeType();
        List<Long> groupIds = groupRepository.findAllGroupIds(now);
        switch (challenge){
            case 0:
                // 걷기 순위 계산
                for (Long groupId : groupIds) {
                    List<User> users = groupRepository.findUsersByGroupId(groupId);
                    calculateWalkingRanks(groupId, now, users);
                }
                break;

            case 1:
                // 기상 순위 계산
                for (Long groupId : groupIds) {
                    List<User> users = groupRepository.findUsersByGroupId(groupId);
                    calculateWakeupRanks(groupId, now, users);
                }
                break;

            case 2:
                // 퀴즈 순위 계산
                for (Long groupId : groupIds) {
                    List<User> users = groupRepository.findUsersByGroupId(groupId);
                    calculateQuizRanks(groupId, now, users);
                }
        }
    }

    private void calculateWalkingRanks(Long groupId, LocalDate now, List<User> users) {
        List<WalkChallenge> records = walkChallengeRepository.findByWalkDateAndGroupId(now, groupId);

        // userCode별로 가장 최신의 step 데이터를 가져오기
        Map<UUID, WalkChallenge> latestWalkChallenges = records.stream()
                .collect(Collectors.toMap(
                        wc -> wc.getUser().getUserCode(),
                        wc -> wc,
                        (wc1, wc2) -> wc1.getWalkDate().isAfter(wc2.getWalkDate()) ? wc1 : wc2
                ));

        // 걸음수가 많은 순서로 정렬
        List<WalkChallenge> sortedRecords = latestWalkChallenges.values().stream()
                .sorted((r1, r2) -> Integer.compare(r2.getStep(), r1.getStep()))
                .collect(Collectors.toList());

        for (int index = 0; index < sortedRecords.size(); index++) {
            WalkChallenge record = sortedRecords.get(index);
            int rank = index + 1;

            DailyRank existingRank = dailyRankRepository.findByGroupIdAndDailyDateAndUser(groupId, now, record.getUser())
                    .orElse(null);

            if (existingRank != null) {
                updateDailyRank(existingRank, rank, users.size());
            } else {
                DailyRank newRank = createDailyRank(groupId, now, record, users.size(), rank);
                dailyRankRepository.save(newRank);
            }
        }
    }

    private void calculateWakeupRanks(Long groupId, LocalDate now, List<User> users) {
        List<WakeupChallenge> records = wakeupChallengeRepository.findByWakeupDateAndGroupId(now, groupId);

        // 일어난 시간이 빠른 순서로 정렬
        records.sort((r1, r2) -> r1.getWakeupTime().compareTo(r2.getWakeupTime()));

        for (int index = 0; index < records.size(); index++) {
            WakeupChallenge record = records.get(index);
            int rank = index + 1;

            DailyRank existingRank = dailyRankRepository.findByGroupIdAndDailyDateAndUser(groupId, now, record.getUser())
                    .orElse(null);

            if (existingRank != null) {
                updateDailyRank(existingRank, rank, users.size());
            } else {
                DailyRank newRank = createDailyRank(groupId, now, record, users.size(), rank);
                dailyRankRepository.save(newRank);
            }
        }
    }

    private void calculateQuizRanks(Long groupId, LocalDate now, List<User> users) {
        List<QuizChallenge> records = quizChallengeRepository.findByQuizDateAndGroupId(now, groupId);

        // 맞춘 퀴즈가 많은 순서로 정렬
        records.sort((r1, r2) -> Integer.compare(r2.getQuizScore(), r1.getQuizScore()));

        for (int index = 0; index < records.size(); index++) {
            QuizChallenge record = records.get(index);
            int rank = index + 1;

            DailyRank existingRank = dailyRankRepository.findByGroupIdAndDailyDateAndUser(groupId, now, record.getUser())
                    .orElse(null);

            if (existingRank != null) {
                updateDailyRank(existingRank, rank, users.size());
            } else {
                DailyRank newRank = createDailyRank(groupId, now, record, users.size(), rank);
                dailyRankRepository.save(newRank);
            }
        }
    }

    private void updateDailyRank(DailyRank existingRank, int rank, int userCount) {
        double dailyRate = calculateDailyRate(rank, userCount);
        double totalRate = existingRank.getTotalRate() - existingRank.getDailyRate() + dailyRate;

        existingRank.setDailyRank(rank);
        existingRank.setDailyRate(dailyRate);
        existingRank.setTotalRate(totalRate);

        dailyRankRepository.save(existingRank);
    }

    private DailyRank createDailyRank(Long groupId, LocalDate now, Object record, int userCount, int rank) {
        User user = null;
        double dailyRate = 0.0;

        if (record instanceof WalkChallenge) {
            user = ((WalkChallenge) record).getUser();
            dailyRate = calculateDailyRate(rank, userCount);
        } else if (record instanceof WakeupChallenge) {
            user = ((WakeupChallenge) record).getUser();
            dailyRate = calculateDailyRate(rank, userCount);
        } else if (record instanceof QuizChallenge) {
            user = ((QuizChallenge) record).getUser();
            dailyRate = calculateDailyRate(rank, userCount);
        }

        double totalRate = dailyRankRepository.findTop1ByUserOrderByDailyDateDesc(Objects.requireNonNull(user).getUserCode())
                .stream()
                .findFirst()
                .map(DailyRank::getTotalRate)
                .orElse(3.5) + dailyRate;

        return DailyRank.builder()
                .dailyDate(now)
                .dailyRank(rank)
                .dailyRate(dailyRate)
                .totalRate(totalRate)
                .group(groupRepository.findById(groupId).orElseThrow(() -> new BaseException(BaseResponseStatus.GROUPS_EMPTY_GROUP_ID)))
                .user(user)
                .build();
    }

    private double calculateDailyRate(int rank, int userCount) {
        double rate = 0.0;

        switch (userCount) {
            case 5:
                switch (rank) {
                    case 1: rate = 0.051; break;
                    case 2: rate = 0.026; break;
                    case 3: rate = 0.0; break;
                    case 4: rate = -0.026; break;
                    case 5: rate = -0.051; break;
                }
                break;
            case 4:
                switch (rank) {
                    case 1: rate = 0.051; break;
                    case 2: rate = 0.026; break;
                    case 3: rate = -0.026; break;
                    case 4: rate = -0.051; break;
                }
                break;
            case 3:
                switch (rank) {
                    case 1: rate = 0.051; break;
                    case 2: rate = 0.0; break;
                    case 3: rate = -0.051; break;
                }
                break;
            case 2:
                switch (rank) {
                    case 1: rate = 0.051; break;
                    case 2: rate = -0.051; break;
                }
                break;
        }

        return rate;
    }

    @Transactional(readOnly = true)
    public DailyRankResponse getDailyRanks(Long groupId) {
        LocalDate today = LocalDate.now();
        List<DailyRank> dailyRanks = dailyRankRepository.findByGroupIdAndDailyDate(groupId, today);

        List<RankDto> ranking = dailyRanks.stream()
                .map(dr -> RankDto.builder()
                        .name(dr.getUser().getName())
                        .dailyRate(String.format("%.3f", dr.getDailyRate()))
                        .challengeType(getChallengeType(today))
                        .challengeDate(today.toString())
                        .build())
                .collect(Collectors.toList());

        return DailyRankResponse.builder()
                .date(today.toString())
                .ranking(ranking)
                .build();
    }

    private int getChallengeType(LocalDate date) {
        ChallengeRecord challengeRecord = challengeRecordRepository.findByChallengeDate(date);
        return challengeRecord.getChallengeType();
    }
}
