package com.hana.api.depositsProduct.entity;


import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity(name = "deposits_product")
@Table(name = "deposits_product")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class DepositsProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DepositsType type;

    private String finPrdtCd; // 금융상품코드
    private String dclsMonth; // 공시제출일
    private String finCoNo; // 금융회사코드
    private String korCoNm; // 금융회사명
    private String finPrdtNm; // 금융상품명
    private String joinWay; // 가입방법
    private String mtrtInt; // 만기 후 이자율
    private String spclCnd; // 우대조건
    private String joinDeny; // 가입제한
    private String joinMember; // 가입대상
    private String etcNote; // 기타 유의사항
    private String maxLimit; // 최고한도
    private String dclsStrtDay; // 공시시작일
    private String dclsEndDay; // 공시종료일
    private String finCoSubmDay; // 금융회사 제출일
}