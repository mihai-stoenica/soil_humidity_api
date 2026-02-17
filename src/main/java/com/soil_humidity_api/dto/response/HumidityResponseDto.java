package com.soil_humidity_api.dto.response;

import java.time.LocalDateTime;

public record HumidityResponseDto(
        Long id,
        Integer value,
        LocalDateTime timestamp
) {
}
