package com.soil_humidity_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PresetDto(
        @NotNull
        @Min(value = 1)
        Integer watering_time
) {
}
