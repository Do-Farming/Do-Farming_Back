package com.hana.api.weeklyRate.service;

import com.hana.api.challenge.entity.ChallengeRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklyRateService {

    @Transactional
    public void insertDailyChallengeRecord() {
        LocalDate today = LocalDate.now();
        int randomNumber = (int) (Math.random() * 3);
        ChallengeRecord challengeRecord = ChallengeRecord.builder()
                                            .challengeDate(today.plusDays(1).atStartOfDay())
                                            .challengeType(randomNumber).build();
    }
}
