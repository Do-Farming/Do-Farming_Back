package com.hana.api.card.dto;

import com.hana.api.card.entity.Card;
import lombok.*;

import java.util.List;

public class CardResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetCardList {
        private List<Card> cardList;
    }

}