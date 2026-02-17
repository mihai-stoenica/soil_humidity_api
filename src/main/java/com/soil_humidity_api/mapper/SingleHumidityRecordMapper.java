package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.response.SingleHumidityResponseDto;
import com.soil_humidity_api.entity.Humidity;
import org.springframework.stereotype.Component;

@Component
public class SingleHumidityRecordMapper {
    public SingleHumidityResponseDto toDto(Humidity humidity) {
        return new SingleHumidityResponseDto(
                humidity.getId(),
                humidity.getValue(),
                humidity.getTimestamp()
        );
    }
}
