package com.hana.api.sms.controller;

import com.hana.api.sms.service.SmsService;
import com.hana.common.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @GetMapping("/send")
    public BaseResponse.SuccessResult<Boolean> sendSms(@RequestParam("phoneNumber") String phoneNumber) {
        return BaseResponse.success(smsService.sendCertificationMessage(phoneNumber));
    }

}
