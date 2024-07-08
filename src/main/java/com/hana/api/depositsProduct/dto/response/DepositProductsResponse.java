package com.hana.api.depositsProduct.dto.response;

import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.api.depositsProduct.entity.DepositsType;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
public class DepositProductsResponse {
    private String prodCode;
    private String prodName;
    private String joinPeriod;
    private String prodRate;
    private DepositsType type;

    public static DepositProductsResponse fromEntity(DepositsProduct depositsProduct) {
        return  DepositProductsResponse.builder().joinPeriod(depositsProduct.getEtcNote())
                .prodCode(String.valueOf(depositsProduct.getId()))
                .prodName(depositsProduct.getFinPrdtNm())
                .prodRate(depositsProduct.getEtcNote())
                .type(depositsProduct.getType()).build();
    }
}
