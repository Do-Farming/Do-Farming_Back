package com.hana.common.job;

import com.hana.api.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UpdateCardListDataTasklet implements Tasklet {

    private final CardService cardService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        cardService.updateCardListData();
        return RepeatStatus.FINISHED;
    }
}