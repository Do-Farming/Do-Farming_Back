package com.hana.api.user.dto.response;

import com.hana.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class MyInfoResponse {
    private UUID userCode;
    private String name;
    private Integer user_img;

    public MyInfoResponse(User user) {
        this.userCode = user.getUserCode();
        this.name = user.getName();
        this.user_img = user.getUserImg();
    }
}
