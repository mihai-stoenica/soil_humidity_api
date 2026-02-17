package com.soil_humidity_api.dto.ws;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SensorDataDto(
        @NotNull
        @Min(value = 0)
        @Max(value = 100)
        Integer humidity,

        @NotNull
        Float temperature
){
}
