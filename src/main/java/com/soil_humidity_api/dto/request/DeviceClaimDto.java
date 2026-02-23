package com.soil_humidity_api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DeviceClaimDto(
        @NotNull
        String apiKey,

        @NotNull
        String name,

        @NotNull
        String secret,

        @NotNull
        @Min(value = 1, message = "Minimum number of seconds is 1.")
        @Max(value = 50, message = "Maximum number of seconds is 50.")
        Integer watering_time
) { }
