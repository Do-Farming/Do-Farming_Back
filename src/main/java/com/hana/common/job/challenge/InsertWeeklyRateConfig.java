package com.hana.common.job.challenge;

import com.hana.common.job.InsertDailyChallengeTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@Slf4j
public class InsertWeeklyRateConfig  extends DefaultBatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final InsertWeeklyRateTasklet insertWeeklyTasklet;

    @Bean
    public Job insertWeeklyRateJob() {
        return new JobBuilder("insertWeeklyJob", jobRepository)
                .start(insertWeeklyRateStep())
                .build();
    }

    @Bean
    @JobScope
    public Step insertWeeklyRateStep() {
        return new StepBuilder("insertWeeklyStep", jobRepository)
                .tasklet(insertWeeklyTasklet, transactionManager)
                .build();
    }
}
