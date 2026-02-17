package com.soil_humidity_api.dto.response;

import java.util.List;

public record HumidityResponseDto(
        List<SingleHumidityResponseDto> records,
        Integer totalPages
) {
}
