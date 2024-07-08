package com.hana.api.sms.entity;

import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "sms_reservation")
@Table(name = "sms_reservation")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
public class SmsReservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String content;
    private LocalDate sendingDate;
    private Boolean isSent = Boolean.FALSE;

    public void setSentTrue(){
        isSent = Boolean.TRUE;
    }
}
