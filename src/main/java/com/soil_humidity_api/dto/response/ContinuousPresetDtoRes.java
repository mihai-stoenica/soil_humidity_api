package com.soil_humidity_api.dto.response;

import com.soil_humidity_api.enums.Pattern;

public record ContinuousPresetDtoRes
(
    Long id,
    Integer watering_time,
    Pattern pattern,
    String name
) implements PresetResponse{ }
