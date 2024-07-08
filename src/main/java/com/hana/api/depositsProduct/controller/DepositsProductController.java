package com.hana.api.depositsProduct.controller;


import com.hana.api.depositsProduct.dto.CheckingRequest;
import com.hana.api.depositsProduct.dto.PreferenceInfo;
import com.hana.api.depositsProduct.dto.response.DepositProductsResponse;
import com.hana.api.depositsProduct.dto.response.GetDetailDepositsProductResponse;
import com.hana.api.depositsProduct.dto.response.GetListDepositsProductResponse;
import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.api.depositsProduct.entity.DepositsType;
import com.hana.api.depositsProduct.service.DepositsProductService;
import com.hana.common.config.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/deposits-products")
public class DepositsProductController {
    private final DepositsProductService depositsProductService;

    @Operation(summary = "예적금 상품 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "조회 완료", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
    })
    @GetMapping("/list")
    public BaseResponse.SuccessResult<Map<DepositsType, List<DepositProductsResponse>>> getDepositsProducts() {
        return BaseResponse.success(depositsProductService.getList());
    }

    @Operation(summary = "예적금 상품 검색")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "검색 완료", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
    })
    @GetMapping("/search")
    public BaseResponse.SuccessResult<List<GetListDepositsProductResponse>> searchDepositsProducts(
            @RequestParam String searchword) {
        List<GetListDepositsProductResponse> response = depositsProductService.searchList(searchword);
        return BaseResponse.success(response);
    }

    @Operation(summary = "예적금 상품 상세 조회")
    @Parameter(name = "id", description = "조회하고 싶은 deposits product id 입력")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
            @ApiResponse(responseCode = "7600", description = "존재하지 않는 deposit product id", content = @Content(schema = @Schema(implementation = BaseResponse.ErrorResult.class))),
    })
    @GetMapping("/detail")
    public BaseResponse.SuccessResult<GetDetailDepositsProductResponse> getProductDetail(@RequestParam("id") Long id) {
        GetDetailDepositsProductResponse response = new GetDetailDepositsProductResponse(
                depositsProductService.getDetail(id));
        return BaseResponse.success(response);
    }

    @GetMapping("/save")
    @Operation(summary = "예적금 상품 저장")
    public BaseResponse.SuccessResult<Boolean> saveProduct() {
        depositsProductService.saveSavingProduct();
        depositsProductService.saveDepositProduct();
        return BaseResponse.success(true);
    }

    @PostMapping("/checking")
    @Operation(summary = "입출금 상품 저장")
    public BaseResponse.SuccessResult<String> saveCheckingProduct(CheckingRequest.CheckingCreateReq request) {
        depositsProductService.saveCheckingProduct(request);
        return BaseResponse.success("입출금 상품 저장 성공");
    }

    @GetMapping("/checking")
    @Operation(summary = "입출금 상품 조회")
    public BaseResponse.SuccessResult<List<DepositsProduct>> getCheckingProduct() {
        return BaseResponse.success(depositsProductService.getCheckingProduct());
    }

    @GetMapping("/preference")
    @Operation(summary = "상품 조회 순위 조회")
    public BaseResponse.SuccessResult<Map<Integer, PreferenceInfo>> getPreferences() {
        return BaseResponse.success(depositsProductService.getTopProduct());
    }

    @GetMapping("/preference/perday")
    @Operation(summary = "특정 상품의 일별 조회수")
    public BaseResponse.SuccessResult<Map<String,Integer>> getPerDayPreferences(@RequestParam(name = "productName") String productName) {
        return BaseResponse.success(depositsProductService.getViewCountPerDay(productName));
    }
}
