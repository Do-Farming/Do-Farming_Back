package com.hana.api.history.service;

import com.hana.api.history.dto.DailySettlementDto;
import com.hana.api.history.dto.HistoryDto;
import com.hana.api.history.dto.TransferHistoryDto;
import com.hana.api.history.entity.History;
import com.hana.api.history.repository.HistoryRepository;
import com.hana.common.config.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private static final Logger log = LoggerFactory.getLogger(HistoryService.class);
    private final HistoryRepository historyRepository;

    public List<HistoryDto> getHistoryByAccountId(Long accountId) {
        List<History> histories = historyRepository.findByAccountId(accountId);
        return histories.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<DailySettlementDto> getStatistics(LocalDate dealdate) {
        LocalDateTime startDate = dealdate.atStartOfDay();
        LocalDateTime endDate = dealdate.atTime(LocalTime.MAX);
        return historyRepository.getStatistics(startDate, endDate);
    }

    @Transactional
    public void historySave(TransferHistoryDto history) {
        try {
            historyRepository.save(history.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(BaseResponseStatus.FAIL_TRANSFER);
        }

    }

    // 엔티티를 DTO로 변환
    private HistoryDto convertToDto(History history) {
        return HistoryDto.builder().
                id(history.getId())
                .accountNumber(history.getAccount().getId())
                .dealdate(history.getDealdate())
                .dealClassification(history.getDealClassification())
                .amount(history.getAmount())
                .recipient(history.getRecipient())
                .remainBalance(history.getBalance())
                .recipientBank(history.getRecipientBank())
                .recipientNumber(history.getRecipientNumber())
                .sender(history.getSender())
                .recipientRemarks(history.getRecipientRemarks())
                .senderRemarks(history.getSenderRemarks())
                .build();
    }
}