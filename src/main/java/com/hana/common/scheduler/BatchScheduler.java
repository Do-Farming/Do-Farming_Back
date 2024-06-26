package com.hana.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
        jobProcessor.setJobRegistry(jobRegistry);
        return jobProcessor;
    }

    @Scheduled(cron = "0 10 0 * * ?") // 매일 00:10:00 에 실행
    public void runJob() {
        LocalDate dealDate = LocalDate.now().minusDays(1);
        try {
            Job job = jobRegistry.getJob("settlementJob"); // job 이름
            JobParametersBuilder jobParam = new JobParametersBuilder()
                    .addLocalDate("dealDate", dealDate)
                    .addLocalDateTime("runAt", LocalDateTime.now());

            jobLauncher.run(job, jobParam.toJobParameters());
        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }

    // 자동이체 스케줄러
    //cron="0/10 * * * * *"
    @Scheduled(cron = "0 9 0 * * ?")  // 매일 9시 실행
    public void runAutoTranferJob() {
        LocalDate today = LocalDate.now();
        //int day = today.getDayOfMonth();

        try {
            Job job = jobRegistry.getJob("autoTransferJob"); // job 이름
            JobParametersBuilder jobParam = new JobParametersBuilder()
                    .addLocalDate("today", today)
                    .addLocalDateTime("runAt", LocalDateTime.now());

            jobLauncher.run(job, jobParam.toJobParameters());
        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }

    }
}