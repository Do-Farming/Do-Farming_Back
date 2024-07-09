package com.hana.api.user.dto.request;

import com.hana.api.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Random;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SignUpRequest {

    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String identificationNumber;
    @NotEmpty
    private String deviceId;

    public User toEntity() {

        Random random = new Random();
        int randomImg = 1 + random.nextInt(4);

        return User.builder()
                .userCode(UUID.randomUUID())
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .status(0)
                .userImg(randomImg)
                .deviceId(deviceId)
                .identificationNumber(identificationNumber).
                build();
    }
}
