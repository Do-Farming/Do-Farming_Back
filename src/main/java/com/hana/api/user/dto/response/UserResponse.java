package com.hana.api.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hana.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private UUID userCode;
    private String name;
    private String phoneNumber;
    private String identificationNumber;

    public UserResponse(User entity){
        this.userCode = entity.getUserCode();
        this.name = entity.getName();
        this.phoneNumber = entity.getPhoneNumber();
        this.identificationNumber = entity.getIdentificationNumber();
    }
}
