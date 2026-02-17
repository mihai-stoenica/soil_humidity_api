package com.soil_humidity_api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record HumidityRequestDto(
    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    Integer value
) { }
