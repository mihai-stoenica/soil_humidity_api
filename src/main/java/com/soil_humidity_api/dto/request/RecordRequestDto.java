package com.soil_humidity_api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RecordRequestDto(
    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    Integer humidity,

    @NotNull
    @Min(value = -20)
    @Max(value = 50)
    Float temperature,

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    Float light_level
) { }
