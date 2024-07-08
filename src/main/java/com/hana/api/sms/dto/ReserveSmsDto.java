package com.hana.api.sms.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
public class ReserveSmsDto {
    private String content;
    private LocalDate sendingDate;
    private List<Long> customerIdList;
}
