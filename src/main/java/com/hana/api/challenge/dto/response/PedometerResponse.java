package com.hana.api.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedometerResponse {
    private String name;
    private int step;
    private double rate;
}
