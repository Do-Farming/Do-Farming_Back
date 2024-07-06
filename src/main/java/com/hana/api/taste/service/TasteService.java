package com.hana.api.taste.service;

import com.hana.api.card.entity.Card;
import com.hana.api.card.repository.CardRepository;
import com.hana.api.taste.entity.Taste;
import com.hana.api.taste.repository.TasteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TasteService {

    private final TasteRepository tasteRepository;
    private final CardRepository cardRepository;

    public List<Taste> getTasteList(String category) {
        List<Taste> allTasteList = tasteRepository.findTasteByTasteCategory(category);
        Collections.shuffle(allTasteList);
        // 8(8강) 크기의 서브 리스트를 반환
        return allTasteList.subList(0, 8);
    }

    public Card getTopRankingCard(String tag) {
        return cardRepository.findTopRankingCardByTag(tag).get();
    }

}