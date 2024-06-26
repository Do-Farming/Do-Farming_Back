package com.hana.common.job;


import com.hana.api.history.dto.DailySettlementDto;
import com.hana.api.history.service.HistoryService;
import com.hana.api.statistics.service.StatisticsSettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class SettlementCalculateTasklet implements Tasklet, StepExecutionListener {

    private final LocalDate dealDate;
    private final HistoryService historyService;
    private final StatisticsSettlementService statisticsSettlementService;

    List<DailySettlementDto> dailySettlementDtoList;

    @Autowired
    public SettlementCalculateTasklet(LocalDate dealDate, HistoryService historyService, StatisticsSettlementService statisticsSettlementService) {
        this.dealDate = dealDate;
        this.historyService = historyService;
        this.statisticsSettlementService = statisticsSettlementService;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // 어제 이체 히스토리에서 통계값 조회
        dailySettlementDtoList = historyService.getStatistics(dealDate);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 통계값을 Statistics Settlement 엔티티에 추가
        statisticsSettlementService.save(dealDate, dailySettlementDtoList);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
