package com.hana.api.card.entity;


import com.hana.api.depositsProduct.entity.DepositsType;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity(name = "card")
@Table(name = "card")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;
    private String cardName;
    private String ranking;
    private String type;
    private String benefit;
    private String img;
    private String corp;
    private String annualFee;

 }