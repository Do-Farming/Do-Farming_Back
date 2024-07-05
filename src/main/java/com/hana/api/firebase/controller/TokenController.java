package com.hana.api.firebase.controller;

import com.hana.api.auth.Auth;
import com.hana.api.user.entity.User;
import com.hana.api.user.service.UserService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/register-token")
public class TokenController {
    final private UserService userService;

    //사용자의 token을 이용해 push하기 위해 받아옴 user db에 auth를 이용해 저장하기
    @Operation(summary = "토큰 주입")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "수정성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
            @ApiResponse(responseCode = "5000", description = "수정실패", content = @Content(schema = @Schema(implementation = BaseResponse.ErrorResult.class))),
    })
    @PostMapping
    public BaseResponse.SuccessResult<UUID> registerToken(@RequestBody Map<String, String> request, @Auth String usercode) {
        String token = request.get("token");
        log.info("++++++++++++++");
        log.info(token);
        UUID code = UUID.fromString(usercode);
        User user = userService.findbyUser(code);
        if(user != null){
            user.setDeviceId(token);
        }
        System.out.println("Token registered: " + token);

        return BaseResponse.success(userService.save(user).getUserCode());
    }
}
