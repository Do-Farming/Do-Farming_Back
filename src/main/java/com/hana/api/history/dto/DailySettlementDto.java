package com.hana.api.history.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailySettlementDto {
    private String dealClassification;
    private Long count;
    private Long sumAmount;
}