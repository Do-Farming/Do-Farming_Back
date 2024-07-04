package com.hana.common.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class InsertDailyChallengeConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final InsertDailyChallengeTasklet insertDailyChallengeTasklet;

    @Bean
    public Job insertDailyChallengeJob() {
        return new JobBuilder("insertDailyChallengeJob", jobRepository)
                .start(insertDailyChallengeStep())
                .build();
    }

    @Bean
    public Step insertDailyChallengeStep() {
        return new StepBuilder("insertDailyChallengeStep", jobRepository)
                .tasklet(insertDailyChallengeTasklet, transactionManager)
                .build();
    }
}
