package com.hana.api.taste.dto;

import com.hana.api.card.entity.Card;
import com.hana.api.taste.entity.Taste;
import lombok.*;

import java.util.List;

public class TasteResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetTasteList {
        private List<Taste> tasteList;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetTopRankingCard {
        private Card card;
    }

}