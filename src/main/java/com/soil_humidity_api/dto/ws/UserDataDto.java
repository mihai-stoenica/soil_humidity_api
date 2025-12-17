package com.soil_humidity_api.dto.ws;

import jakarta.validation.constraints.NotNull;

public record UserDataDto(
    @NotNull
    Integer command,
    @NotNull
    Long deviceId
) {
}
