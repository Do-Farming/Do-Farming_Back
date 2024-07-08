package com.hana.common.job.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@Slf4j
public class CalculateDailyRanksConfig extends DefaultBatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CalculateDailyRanksTasklet calculateDailyRanksTasklet;

    @Bean
    public Job calculateDailyRanksJob() {
        return new JobBuilder("calculateDailyRanksJob", jobRepository)
                .start(calculateDailyRanksStep())
                .build();
    }

    @Bean
    @JobScope
    public Step calculateDailyRanksStep() {
        return new StepBuilder("calculateDailyRanksStep", jobRepository)
                .tasklet(calculateDailyRanksTasklet, transactionManager)
                .build();
    }
}
