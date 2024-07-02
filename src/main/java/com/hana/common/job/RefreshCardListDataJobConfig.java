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
public class RefreshCardListDataJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RefreshCardListDataTasklet refreshCardListDataTasklet;

    @Bean
    public Job refreshCardListDataJob() {
        return new JobBuilder("refreshCardListDataJob", jobRepository)
                .start(refreshCardListDataStep())
                .build();
    }

    @Bean
    public Step refreshCardListDataStep() {
        return new StepBuilder("refreshCardListDataStep", jobRepository)
                .tasklet(refreshCardListDataTasklet, transactionManager)
                .build();
    }
}