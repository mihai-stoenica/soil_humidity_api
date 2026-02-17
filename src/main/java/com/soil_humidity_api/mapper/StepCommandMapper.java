package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.ws.StepSensorCommandDto;
import com.soil_humidity_api.dto.ws.UserDataDto;
import com.soil_humidity_api.entity.Preset;
import org.springframework.stereotype.Component;

@Component
public class StepCommandMapper {
    public StepSensorCommandDto toDto(Preset preset, UserDataDto payload) {
        return new StepSensorCommandDto(payload.command(), preset.getWatering_time(), preset.getPattern(), preset.getSteps(), preset.getDelay());
    }
}
