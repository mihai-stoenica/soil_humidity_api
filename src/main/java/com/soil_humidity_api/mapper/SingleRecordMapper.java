package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.response.SingleRecordResponseDto;
import com.soil_humidity_api.entity.Record;
import org.springframework.stereotype.Component;

@Component
public class SingleRecordMapper {
    public SingleRecordResponseDto toDto(Record record) {
        return new SingleRecordResponseDto(
                record.getId(),
                record.getHumidity(),
                record.getTemperature(),
                record.getLight_level(),
                record.getTimestamp()
        );
    }
}
