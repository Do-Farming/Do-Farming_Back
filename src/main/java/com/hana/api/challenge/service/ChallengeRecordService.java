package com.hana.api.challenge.service;

import com.hana.api.card.entity.Card;
import com.hana.api.challenge.entity.ChallengeRecord;
import com.hana.api.challenge.repository.ChallengRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeRecordService {

    final ChallengRecordRepository challengRecordRepository;
    @Transactional
    public void insertDailyChallengeRecord() {
        LocalDate today = LocalDate.now();
        int randomNumber = (int) (Math.random() * 3);
        ChallengeRecord challengeRecord = ChallengeRecord.builder()
                                            .challengeDate(today.plusDays(1))
                                            .challengeType(randomNumber).build();
        challengRecordRepository.save(challengeRecord);
    }
}
