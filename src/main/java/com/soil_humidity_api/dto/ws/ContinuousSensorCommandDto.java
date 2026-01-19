package com.soil_humidity_api.dto.ws;

import jakarta.validation.constraints.NotNull;

public record ContinuousSensorCommandDto(
        @NotNull
        Integer command,
        @NotNull
        Integer duration,
        com.soil_humidity_api.enums.@NotNull Pattern pattern
) {
}
