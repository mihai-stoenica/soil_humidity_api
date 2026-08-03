package com.soil_humidity_api.dto.response;

import java.time.LocalDateTime;

public record SingleRecordResponseDto(
        Long id,
        Integer humidity,
        Float temperature,
        Float light_level,
        LocalDateTime timestamp
) {
}
