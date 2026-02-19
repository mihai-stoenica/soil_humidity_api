package com.soil_humidity_api.dto.request;

import com.soil_humidity_api.enums.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StepPresetDto(
        @NotNull
        @Min(value = 1, message = "The watering time must be greater than 0.")
        Integer watering_time,

        @NotNull
        Pattern pattern,

        @NotNull
        @Min(value = 1, message = "The number of steps must be greater than 0.")
        Integer steps,

        @NotNull
        @Min(value = 1, message = "The delay must be greater than 0.")
        Integer delay
) implements PresetRequest {
}
