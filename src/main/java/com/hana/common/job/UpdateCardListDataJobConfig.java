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
public class UpdateCardListDataJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UpdateCardListDataTasklet updateCardListDataTasklet;

    @Bean
    public Job updateCardListDataJob() {
        return new JobBuilder("updateCardListDataJob", jobRepository)
                .start(updateCardListDataStep())
                .build();
    }

    @Bean
    public Step updateCardListDataStep() {
        return new StepBuilder("updateCardListDataStep", jobRepository)
                .tasklet(updateCardListDataTasklet, transactionManager)
                .build();
    }
}