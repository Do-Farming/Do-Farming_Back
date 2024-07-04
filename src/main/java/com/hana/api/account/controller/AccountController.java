package com.hana.api.account.controller;


import com.hana.api.account.dto.AccountCheckResponse;
import com.hana.api.account.dto.AccountValidationRequest;
import com.hana.api.account.dto.MakeTransactionDto;
import com.hana.api.account.dto.NormalAccountCreateDto;
import com.hana.api.account.service.AccountService;
import com.hana.api.auth.Auth;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "계좌 개설")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "계좌개설 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
    })

    @PostMapping("/create")
    public BaseResponse.SuccessResult<AccountCheckResponse> createAccount(@Auth String userCode,
                                                                          @RequestBody NormalAccountCreateDto accountCreateDto) {
        System.out.println("test1 "+userCode);
        System.out.println("test1"+accountCreateDto.toString());

        UUID uuid = UUID.fromString(userCode);
        return BaseResponse.success(
                new AccountCheckResponse(accountService.createAccount(uuid, accountCreateDto)));
    }

    @Operation(summary = "계좌 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "계좌조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
    })
    @GetMapping("/my")
    public BaseResponse.SuccessResult<List<AccountCheckResponse>> myAccounts(@Parameter(hidden = true) @Auth String userCode) {

        UUID uuid = UUID.fromString(userCode);
        return BaseResponse.success(accountService.myAccounts(uuid));
    }

    @Operation(summary = "단일 계좌 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "단일 계좌조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
    })
    @GetMapping("/get")
    public BaseResponse.SuccessResult<AccountCheckResponse> getMyAccounts(@RequestParam Long accountId) {
        return BaseResponse.success(new AccountCheckResponse(accountService.getAccount(accountId)));
    }

    @Operation(summary = "계좌 이체")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "계좌이체 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
            @ApiResponse(responseCode = "3300", description = "계좌이체 실패", content = @Content(schema = @Schema(implementation = BaseResponse.ErrorResult.class))),
    })
    @PostMapping("/simple")
    public BaseResponse.SuccessResult<MakeTransactionDto> simpleTransfer(@Auth Long customerId,
                                                                         @RequestBody MakeTransactionDto makeTransactionDto) {
        log.info("계좌이체 시도 : {}", makeTransactionDto.toString());
        return BaseResponse.success(accountService.makeTransaction(makeTransactionDto));
    }

    @Operation(summary = "계좌 비밀번호 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "비밀번호 일치", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
    })
    @PostMapping("/validate")
    public BaseResponse.SuccessResult<Boolean> validateAccount(@RequestBody AccountValidationRequest validationRequest) {
        return BaseResponse.success(accountService.validateAccount(validationRequest));
    }


}