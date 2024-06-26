package com.hana.common.job;

import com.hana.api.history.service.HistoryService;
import com.hana.api.statistics.service.StatisticsSettlementService;
import com.hana.common.job.SettlementCalculateTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
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
@Slf4j
public class SettlementJobConfig extends DefaultBatchConfiguration {

    private final HistoryService historyService;
    private final StatisticsSettlementService statisticsSettlementService;

    @Bean
    public Job settlementJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws DuplicateJobException {
        log.info("=== settlementJob ===");
        Job job = new JobBuilder("settlementJob", jobRepository)
                .start(validStep(jobRepository, transactionManager, null))
                .on("FAILED")
                .end()
                .on("*")
                .to(calculateStep(jobRepository, transactionManager, null))
                .end()
                .build();
        return job;
    }
    // 어떤 역할을 할지

    @Bean
    @JobScope
    public Step validStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters[dealDate]}") LocalDate dealDate
    ) {
        log.info("== validStep ==");
        log.info(dealDate.toString());
        return new StepBuilder("settlementValidStep", jobRepository)
                .tasklet(new SettlementValidTasklet(dealDate, statisticsSettlementService), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step calculateStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters[dealDate]}") LocalDate dealDate
    ) {
        log.info("== calculateStep ==");
        log.info(dealDate.toString());
        return new StepBuilder("settlementCalculateStep", jobRepository)
                .tasklet(new SettlementCalculateTasklet(dealDate, historyService, statisticsSettlementService), transactionManager)
                .build();
    }


}
