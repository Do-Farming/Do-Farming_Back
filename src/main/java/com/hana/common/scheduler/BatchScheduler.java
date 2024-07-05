package com.hana.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Bean
    @ConditionalOnMissingBean(name = "jobRegistryBeanPostProcessor")
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

    // 자동이체
    @Scheduled(cron = "0 9 0 * * ?")  // 매일 9시 실행
    public void runAutoTranferJob() {
        LocalDate today = LocalDate.now();

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

    // '카드고릴라'에서 카드 리스트 정보를 불러와 DB에 갱신
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void runUpdateCardListDataJob() {
        try {
            Job job = jobRegistry.getJob("updateCardListDataJob");
            JobParametersBuilder jobParam = new JobParametersBuilder()
                    .addLocalDateTime("runAt", LocalDateTime.now());

            jobLauncher.run(job, jobParam.toJobParameters());
        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }
    @Scheduled(cron = "0 0 12 * * ?") // 매일 정오에 실행
    //@Scheduled(fixedRate = 30000)
    public void insertDailyChallenge() {
        try {
            Job job = jobRegistry.getJob("insertDailyChallengeJob");
            JobParametersBuilder jobParam = new JobParametersBuilder()
                    .addLocalDateTime("runAt", LocalDateTime.now());
            jobLauncher.run(job, jobParam.toJobParameters());
        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }

    //WeeklyRate 매주 일요일 밤 11시 55분에 실행
    @Scheduled(cron = "0 55 23 ? * SUN") // 매일 정오에 실행
    public void insertWeeklyRate() {
        try {
            Job job = jobRegistry.getJob("insertWeeklyRateJob");
            JobParametersBuilder jobParam = new JobParametersBuilder()
                    .addLocalDateTime("runAt", LocalDateTime.now());
            jobLauncher.run(job, jobParam.toJobParameters());
        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }
}
