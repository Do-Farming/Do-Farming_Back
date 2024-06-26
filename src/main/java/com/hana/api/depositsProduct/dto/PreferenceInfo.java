package com.hana.api.depositsProduct.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PreferenceInfo implements Comparable<PreferenceInfo> {
    private String productName;
    private Integer viewCount;

    @Override
    public int compareTo(@NotNull PreferenceInfo o) {
        return Integer.compare(o.viewCount, this.viewCount);
    }
}