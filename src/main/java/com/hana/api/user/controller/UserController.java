package com.hana.api.user.controller;

import com.hana.api.auth.Auth;
import com.hana.api.user.dto.CustomerContactDto;
import com.hana.api.user.dto.request.SignUpRequest;
import com.hana.api.user.dto.response.MyInfoResponse;
import com.hana.api.user.service.UserService;
import com.hana.common.config.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class UserController {
    private final UserService customerService;

    @Operation(summary = "회원 가입")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
            @ApiResponse(responseCode = "5000", description = "이미 가입된 휴대폰 또는 주민등록번호", content = @Content(schema = @Schema(implementation = BaseResponse.ErrorResult.class))),
    })
    @PostMapping("/signup")
    public BaseResponse.SuccessResult<UUID> signUp(@RequestBody SignUpRequest signUpRequest) {
        log.info("회원가입 시도 : {}", signUpRequest);
        return BaseResponse.success(customerService.save(signUpRequest));
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public BaseResponse.SuccessResult<MyInfoResponse> me(@Parameter(hidden = true) @Auth String userCode) {
        return BaseResponse.success(customerService.findByUserCode(UUID.fromString(userCode)));
    }

    @Operation(summary = "[😈Admin] 고객 이름 및 연락처 정보 조회")
    @GetMapping("/contact")
    public BaseResponse.SuccessResult<List<CustomerContactDto>> getCustomerContact() {
        return BaseResponse.success(customerService.getCustomerContact());
    }
}
