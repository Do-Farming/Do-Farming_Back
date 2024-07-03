package com.hana.api.dailyRank.dto.Response;

import com.hana.api.dailyRank.dto.RankDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRankResponse {
    private String date;
    private List<RankDto> ranking;
}