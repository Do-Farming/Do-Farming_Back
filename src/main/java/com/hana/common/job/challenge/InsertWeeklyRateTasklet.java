package com.hana.common.job.challenge;


import com.hana.api.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InsertWeeklyRateTasklet implements Tasklet {

    final GroupService groupService;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        groupService.insertWeeklyRateForGroupMembers();
        return RepeatStatus.FINISHED;
    }
}
