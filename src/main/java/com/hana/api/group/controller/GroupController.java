package com.hana.api.group.controller;


import com.hana.api.auth.Auth;
import com.hana.api.group.dto.GroupRequestDto;
import com.hana.api.group.service.GroupService;
import com.hana.common.config.BaseException;
import com.hana.common.config.BaseResponse;
import com.hana.common.config.BaseResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "챌린지 그룹 생성")
    @PostMapping("/create")
    public BaseResponse.SuccessResult<String> createGroup(@RequestBody GroupRequestDto.GroupCreateReq request, @Auth String userCode) {
        UUID uuid = UUID.fromString(userCode);
        groupService.createGroup(request, uuid);
        return BaseResponse.success("챌린지 그룹 생성 성공");
    }
}
