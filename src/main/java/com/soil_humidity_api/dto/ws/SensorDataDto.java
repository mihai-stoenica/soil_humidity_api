package com.soil_humidity_api.dto.ws;

public record SensorDataDto(
        Integer humidity,
        Float temperature
){
}
