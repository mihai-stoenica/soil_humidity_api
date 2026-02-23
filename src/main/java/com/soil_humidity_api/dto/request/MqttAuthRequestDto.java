package com.soil_humidity_api.dto.request;

import jakarta.validation.constraints.NotNull;

public record MqttAuthRequestDto(
        @NotNull
        String password,

        @NotNull
        String username
) {
}
