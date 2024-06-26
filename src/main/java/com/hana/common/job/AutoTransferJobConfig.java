package com.hana.common.job;

import com.hana.api.account.service.AccountService;
import com.hana.api.autoTransfer.service.AutoTransferService;
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
public class AutoTransferJobConfig extends DefaultBatchConfiguration {

    private final AccountService accountService;
    private final AutoTransferService autoTransferService;

    @Bean
    public Job autoTransferJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws DuplicateJobException {
        log.info("=== autoTransferJob ===");
        Job job = new JobBuilder("autoTransferJob", jobRepository)
                .start(autoTransferStep(jobRepository, transactionManager, null))
                .build();
        return job;
    }
    // 어떤 역할을 할지

    @Bean
    @JobScope
    public Step autoTransferStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters[today]}") LocalDate today
    ) {
        log.info("== autoTransferStep ==");
        //log.info(dealDate.toString());
        return new StepBuilder("autoTransferStep", jobRepository)
                .tasklet(new com.hana.common.job.AutoTransferTasklet(autoTransferService, accountService, today), transactionManager)
                .build();
    }


}
