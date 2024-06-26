package com.hana.api.depositsProduct.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.api.depositsProduct.entity.DepositsType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class DepositsProductResponse {
    @JsonProperty("join_member")
    private String joinMember;

    @JsonProperty("join_way")
    private String joinWay;

    @JsonProperty("mtrt_int")
    private String mtrtInt;

    @JsonProperty("dcls_end_day")
    private String dclsEndDay;

    @JsonProperty("spcl_cnd")
    private String spclCnd;

    @JsonProperty("join_deny")
    private String joinDeny;

    @JsonProperty("max_limit")
    private String maxLimit;

    @JsonProperty("fin_co_no")
    private String finCoNo;

    @JsonProperty("kor_co_nm")
    private String korCoNm;

    @JsonProperty("dcls_month")
    private String dclsMonth;

    @JsonProperty("fin_prdt_nm")
    private String finPrdtNm;

    @JsonProperty("fin_prdt_cd")
    private String finPrdtCd;

    @JsonProperty("etc_note")
    private String etcNote;

    @JsonProperty("dcls_strt_day")
    private String dclsStrtDay;

    @JsonProperty("fin_co_subm_day")
    private String finCoSubmDay;

    public DepositsProduct toDepositEntity() {
        return DepositsProduct.builder()
                .type(DepositsType.DEPOSIT)
                .finPrdtCd(finPrdtCd)
                .dclsMonth(dclsMonth)
                .finCoNo(finCoNo)
                .korCoNm(korCoNm)
                .finPrdtNm(finPrdtNm)
                .joinWay(joinWay)
                .mtrtInt(mtrtInt)
                .spclCnd(spclCnd)
                .joinDeny(joinDeny)
                .joinMember(joinMember)
                .etcNote(etcNote)
                .maxLimit(maxLimit)
                .dclsStrtDay(dclsStrtDay)
                .dclsEndDay(dclsEndDay)
                .finCoSubmDay(finCoSubmDay)
                .build();
    }
    public DepositsProduct toSavingEntity() {
        return DepositsProduct.builder()
                .type(DepositsType.SAVING)
                .finPrdtCd(finPrdtCd)
                .dclsMonth(dclsMonth)
                .finCoNo(finCoNo)
                .korCoNm(korCoNm)
                .finPrdtNm(finPrdtNm)
                .joinWay(joinWay)
                .mtrtInt(mtrtInt)
                .spclCnd(spclCnd)
                .joinDeny(joinDeny)
                .joinMember(joinMember)
                .etcNote(etcNote)
                .maxLimit(maxLimit)
                .dclsStrtDay(dclsStrtDay)
                .dclsEndDay(dclsEndDay)
                .finCoSubmDay(finCoSubmDay)
                .build();
    }
}