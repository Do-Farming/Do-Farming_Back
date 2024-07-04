package com.hana.api.dailyRank.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankDto {
    private String name;
    private String dailyRate;
    private int challengeType;
    private String challengeDate;
}