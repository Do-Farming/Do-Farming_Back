package com.hana.api.statistics.service;


import com.hana.api.history.dto.DailySettlementDto;
import com.hana.api.statistics.entity.StatisticsSettlement;
import com.hana.api.statistics.repository.StatisticsSettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsSettlementService {
    private final StatisticsSettlementRepository repository;
    private final StatisticsSettlementRepository statisticsSettlementRepository;

    @Transactional
    public void save(LocalDate date, List<DailySettlementDto> dailySettlementDtoList) {
        StatisticsSettlement statisticsSettlement = createStatisticsSettlement(date, dailySettlementDtoList);
        repository.save(statisticsSettlement);
    }

    @Transactional
    public void updateByDate(LocalDate date, List<DailySettlementDto> dailySettlementDtoList) {
        StatisticsSettlement statisticsSettlement = createStatisticsSettlement(date, dailySettlementDtoList);
        repository.updateByDate(
                statisticsSettlement.getDepositAmount(),
                statisticsSettlement.getDepositCount(),
                statisticsSettlement.getWithdrawalAmount(),
                statisticsSettlement.getWithdrawalCount(),
                statisticsSettlement.getDate()
        );
    }

    public StatisticsSettlement getByDate(LocalDate dealDate) {
        return statisticsSettlementRepository.findByDate(dealDate).get(0);
    }

    public List<StatisticsSettlement> getAllByDateBetween(LocalDate startDate, LocalDate endDate) {
        return statisticsSettlementRepository.findAllByDateBetween(startDate, endDate);
    }

    public boolean existStatisticsSettlement(LocalDate dealDate) {
        return !statisticsSettlementRepository.findByDate(dealDate).isEmpty();
    }

    public StatisticsSettlement createStatisticsSettlement(LocalDate date, List<DailySettlementDto> dailySettlementDtoList) {
        StatisticsSettlement statisticsSettlement = new StatisticsSettlement();
        statisticsSettlement.setDate(date);

        for (DailySettlementDto dailySettlementDto : dailySettlementDtoList) {
            if (dailySettlementDto.getDealClassification().equals("입금")) {
                statisticsSettlement.setDepositCount(dailySettlementDto.getCount());
                statisticsSettlement.setDepositAmount(dailySettlementDto.getSumAmount());
            } else {
                statisticsSettlement.setWithdrawalCount(dailySettlementDto.getCount());
                statisticsSettlement.setWithdrawalAmount(dailySettlementDto.getSumAmount());
            }
        }

        return statisticsSettlement;
    }
}
