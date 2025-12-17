package com.soil_humidity_api.dto.ws;

public record SensorCommandDto(
        Integer command,
        Integer duration
) {
}
