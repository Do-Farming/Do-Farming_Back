package com.hana.api.taste.service;

import com.hana.api.card.entity.Card;
import com.hana.api.taste.entity.Taste;
import com.hana.api.taste.repository.TasteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TasteService {

    private final TasteRepository tasteRepository;

    public List<Taste> getCardList() {
        List<Taste> tasteList = tasteRepository.findAll();
        return tasteList;
    }

}