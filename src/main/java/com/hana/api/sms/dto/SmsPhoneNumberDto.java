package com.hana.api.sms.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
public class SmsPhoneNumberDto {
    private Long smsReservationTargetId;
    private String phoneNumber;
}
