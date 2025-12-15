package com.soil_humidity_api.dto.response;

public record UserDto(
        Long id,
        String name,
        String email
) {
}
