package com.hana.api.card.entity;

import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "card")
@Table(name = "card")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
public class Card extends BaseEntity {
    @Id
    @Column(name = "card_id")
    private Long id; // idx

    private String cardName; // name

    private int ranking; // ranking

    private String type; // card_gb (CRD, CHK)

    @Column(columnDefinition = "TEXT")
    private String benefit; // top_benefit

    private String img; // card_img

    @Column(columnDefinition = "TEXT")
    private String corp; // corp

    private String annualFee; // annual_fee_basic
 }