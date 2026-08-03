package com.soil_humidity_api.dto.request;

import com.soil_humidity_api.enums.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ContinuousPresetDto(
        @NotNull
        @Min(value = 1, message = "The watering time must be greater than 0.")
        Integer watering_time,

        @NotNull
        Pattern pattern,

        @NotNull
        String name
)  implements PresetRequest {
}
