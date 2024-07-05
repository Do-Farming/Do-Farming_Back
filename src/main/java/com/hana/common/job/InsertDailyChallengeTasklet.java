package com.hana.common.job;

import com.hana.api.challenge.service.ChallengeRecordService;
import com.hana.api.firebase.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Component
public class InsertDailyChallengeTasklet implements Tasklet {

    private final ChallengeRecordService challengeRecordService;
    private final PushNotificationService pushNotificationService;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        pushNotificationService.sendNotificationToDevice("d","d","d");
        return RepeatStatus.FINISHED;
    }
}
