package com.hana.common.job;

import com.hana.api.account.dto.MakeTransactionDto;
import com.hana.api.account.service.AccountService;
import com.hana.api.autoTransfer.dto.CreateAutoTransferDto;
import com.hana.api.autoTransfer.service.AutoTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class AutoTransferTasklet implements Tasklet, StepExecutionListener {
    private final AutoTransferService autoTransferService;
    private final AccountService transferService;
    private LocalDate today;
    private List<CreateAutoTransferDto> autoTransferDtoList;

    public AutoTransferTasklet(AutoTransferService autoTransferService, AccountService transferService, LocalDate today) {
        this.autoTransferService = autoTransferService;
        this.transferService = transferService;
        this.today = today;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        int day = today.getDayOfMonth();
        autoTransferDtoList = autoTransferService.getAutoTransferByDay(day);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        for (CreateAutoTransferDto autoTransferDto : autoTransferDtoList) {
            if (today.isAfter(autoTransferDto.getStartDate()) && today.isBefore(autoTransferDto.getEndDate())) {
                MakeTransactionDto autoTransfer = MakeTransactionDto.builder()
                        .amount(autoTransferDto.getAmount())
                        .recipientBank(autoTransferDto.getRecipientBank())
                        .recipientAccountNumber(autoTransferDto.getToAccountNumber())
                        .recipientRemarks(autoTransferDto.getRecipientRemarks())
                        .senderRemarks(autoTransferDto.getSenderRemarks())
                        .accountId(autoTransferDto.getAccountId())
                        .password(autoTransferDto.getPassword())
                        .build();

                transferService.makeTransaction(autoTransfer);
            }
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
