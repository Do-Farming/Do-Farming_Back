package com.hana.api.group.controller;


import com.hana.api.auth.Auth;
import com.hana.api.group.dto.GroupRequestDto;
import com.hana.api.group.dto.GroupResponseDto;
import com.hana.api.group.entity.Group;
import com.hana.api.group.service.GroupService;
import com.hana.common.config.BaseException;
import com.hana.common.config.BaseResponse;
import com.hana.common.config.BaseResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Operation(summary = "챌린지 그룹 가입")
    @PostMapping("/join")
    public BaseResponse.SuccessResult<String> joinGroup(@RequestBody GroupRequestDto.GroupJoinReq request, @Auth String userCode) {
        // UUID 변환 및 검증
        UUID uuid;
        try {
            uuid = UUID.fromString(userCode);
        } catch (IllegalArgumentException e) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }

        // 그룹 가입 시도
        int result = groupService.joinGroup(request, uuid);
        if (result == 1) {
            return BaseResponse.success("챌린지 그룹 가입 성공");
        } else if (result == 0) {
            throw new BaseException(BaseResponseStatus.GROUPS_ALREADY_START);
        } else if (result == 2) {
            throw new BaseException(BaseResponseStatus.GROUPS_ALREADY_MEMBER);
        } else {
            throw new BaseException(BaseResponseStatus.SYSTEM_ERROR);
        }
    }

    @Operation(summary = "그룹 목록 조회")
    @GetMapping("/list")
    public BaseResponse.SuccessResult<List<GroupResponseDto.GetGroupListRes>> groupList() {
        return BaseResponse.success(groupService.groupList());
    }
}