package com.hana.api.depositsProduct.dto;

import com.hana.api.depositsProduct.entity.DepositsType;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

public class CheckingRequest {
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CheckingCreateReq {
        private DepositsType type;
        @NotEmpty
        private String finPrdtCd;
        @NotEmpty
        private String dclsMonth;
        @NotEmpty
        private String finCoNo;
        @NotEmpty
        private String korCoNm;
        @NotEmpty
        private String finPrdtNm;
        @NotEmpty
        private String joinWay;
        @NotEmpty
        private String mtrtInt;
        @NotEmpty
        private String spclCnd;
        @NotEmpty
        private String joinDeny;
        @NotEmpty
        private String joinMember;
        @NotEmpty
        private String etcNote;
        @NotEmpty
        private String maxLimit;
        @NotEmpty
        private String dclsStrtDay;
        @NotEmpty
        private String dclsEndDay;
        @NotEmpty
        private String finCoSubmDay;
    }
}
