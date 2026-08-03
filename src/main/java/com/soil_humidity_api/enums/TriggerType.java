package com.soil_humidity_api.enums;

import lombok.Getter;

@Getter
public enum TriggerType {
    MANUAL("manual"),
    SCHEDULED("scheduled");

    private final String value;

    TriggerType(String value) {
        this.value = value;
    }

}
