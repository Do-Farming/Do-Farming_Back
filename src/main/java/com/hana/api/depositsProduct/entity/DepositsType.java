package com.hana.api.depositsProduct.entity;

import lombok.Getter;

@Getter
public enum DepositsType {
    DEPOSIT("적금"),
    SAVING("예금"),
    CHECKING("입출금"),
    DOFARMING("두파밍");

    private final String title;

    DepositsType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
