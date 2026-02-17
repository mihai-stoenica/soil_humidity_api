package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.response.HumidityResponseDto;
import com.soil_humidity_api.dto.response.SingleHumidityResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HumidityRecordMapper {
    public HumidityResponseDto toDto(List<SingleHumidityResponseDto> records, Integer totalPages) {
        return new HumidityResponseDto(
                records,
                totalPages
        );
    }
}
