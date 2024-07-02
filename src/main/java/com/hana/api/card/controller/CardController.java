package com.hana.api.card.controller;

import com.hana.api.card.service.CardService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;

    @PutMapping("")
    @Operation(summary = "카드 Top 100 데이터 DB update")
    public BaseResponse.SuccessResult<Boolean> updateCardListData() {
        cardService.updateCardListData();
        return BaseResponse.success(true);
    }

}
