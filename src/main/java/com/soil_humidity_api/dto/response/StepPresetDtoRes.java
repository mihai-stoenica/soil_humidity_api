package com.soil_humidity_api.dto.response;

import com.soil_humidity_api.enums.Pattern;

public record StepPresetDtoRes (
    Long id,
    Integer watering_time,
    Pattern pattern,
    Integer steps,
    Integer delay,
    String name

) implements PresetResponse {}
