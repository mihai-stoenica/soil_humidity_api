package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.response.RecordResponseDto;
import com.soil_humidity_api.dto.response.SingleRecordResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordMapper {
    public RecordResponseDto toDto(List<SingleRecordResponseDto> records, Integer totalPages) {
        return new RecordResponseDto(
                records,
                totalPages
        );
    }
}
