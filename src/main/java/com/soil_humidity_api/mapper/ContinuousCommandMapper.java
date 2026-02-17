package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.ws.ContinuousSensorCommandDto;
import com.soil_humidity_api.dto.ws.UserDataDto;
import com.soil_humidity_api.entity.Preset;
import org.springframework.stereotype.Component;

@Component
public class ContinuousCommandMapper {
    public ContinuousSensorCommandDto toDto(Preset preset, UserDataDto payload) {
        return new ContinuousSensorCommandDto(
                payload.command(),
                preset.getWatering_time(),
                preset.getPattern());
    }
}
