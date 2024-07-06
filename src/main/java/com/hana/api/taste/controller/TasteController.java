package com.hana.api.taste.controller;

import com.hana.api.card.entity.Card;
import com.hana.api.taste.dto.TasteResponseDto;
import com.hana.api.taste.entity.Taste;
import com.hana.api.taste.service.TasteService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/taste")
public class TasteController {

    private final TasteService tasteService;

    @GetMapping("")
    @Operation(summary = "Taste List Data 조회")
    public BaseResponse.SuccessResult<TasteResponseDto.GetTasteList> getTasteList(@RequestParam String category) {
        List<Taste> tasteList = tasteService.getTasteList(category);
        return BaseResponse.success(new TasteResponseDto.GetTasteList(tasteList));
    }

    @GetMapping("/recommended")
    @Operation(summary = "취향 이상형 월드컵 1위와 관련된 추천 신용카드 조회")
    public BaseResponse.SuccessResult<TasteResponseDto.GetTopRankingCard> getTopRankingCard(@RequestParam String tag) {
        Card topRankingCard = tasteService.getTopRankingCard(tag);
        return BaseResponse.success(new TasteResponseDto.GetTopRankingCard(topRankingCard));
    }

}
