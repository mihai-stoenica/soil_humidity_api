package com.soil_humidity_api.dto.response;

import java.time.Instant;

public record DeviceDto(
        Long id,
        String name,
        boolean connected,
        Instant lastSeen,
        Integer lastHumidity,
        Long activePreset
) { }
