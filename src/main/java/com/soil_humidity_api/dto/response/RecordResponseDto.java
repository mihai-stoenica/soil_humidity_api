package com.soil_humidity_api.dto.response;

import java.util.List;

public record RecordResponseDto(
        List<SingleRecordResponseDto> records,
        Integer totalPages
) {
}
