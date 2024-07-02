package com.hana.api.card.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.api.card.entity.Card;
import com.hana.api.card.repository.CardRepository;
import com.hana.api.depositsProduct.dto.PreferenceInfo;
import com.hana.api.depositsProduct.dto.response.DepositsProductResponse;
import com.hana.api.depositsProduct.dto.response.GetListDepositsProductResponse;
import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.api.depositsProduct.repository.DepositsProductRepository;
import com.hana.api.depositsProduct.util.DepositsProductUtil;
import com.hana.common.aop.Preference;
import com.hana.common.config.*;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.LocalDate;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void refreshCardListData() {
        cardRepository.deleteAll();

        List<Card> cardList = new ArrayList<>();
        cardList.addAll(getCardListData("CRD"));
        cardList.addAll(getCardListData("CHK"));

        cardRepository.saveAll(cardList);
    }

    // 외부 API 호출을 통해 card list data 조회하는 함수
    public List<Card> getCardListData(String type) {
        List<Card> cardList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String formattedDate = today.toString();

        String url = String.format("https://api.card-gorilla.com:8080/v1/charts/ranking?date=%s&term=weekly&card_gb=%s&limit=100&chart=top100&idx=&idx2=", formattedDate, type);
//        log.info("카드고릴라 API 요청 url: {}", url);

        try {
            // RestTemplate을 사용하여 외부 API 호출
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // 응답을 JsonNode로 변환하여 처리
            JsonNode cardListResponse = objectMapper.readTree(response);
//            log.info("cardListResponse: {}", cardListResponse.toString());


            if(!cardListResponse.isArray()) return null;
            for(JsonNode jsonNode : cardListResponse) {
                // Response to Entity
                Card card = Card.builder()
                        .id(jsonNode.path("idx").asLong())
                        .cardName(jsonNode.path("name").asText())
                        .ranking(jsonNode.path("ranking").asText())
                        .type(type)
                        .benefit(jsonNode.path("top_benefit").toString())
                        .img("https://d1c5n4ri2guedi.cloudfront.net" + jsonNode.path("card_img").asText())
                        .corp(jsonNode.path("corp").toString())
                        .annualFee(jsonNode.path("annual_fee_basic").asText())
                        .build();

                cardList.add(card);
            }

//            cardList.forEach(card -> {
//                log.info(card.toString());
//            });
        } catch (Exception e) {
            log.error("Error fetching card list data", e);
        }

        return cardList;
    }

}