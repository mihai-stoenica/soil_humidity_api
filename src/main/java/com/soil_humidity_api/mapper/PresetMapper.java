package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.request.ContinuousPresetDto;
import com.soil_humidity_api.dto.request.PresetRequest;
import com.soil_humidity_api.dto.request.StepPresetDto;
import com.soil_humidity_api.entity.Preset;
import com.soil_humidity_api.enums.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PresetMapper {
    public PresetRequest toDto(Preset preset) {
        if (preset.getPattern() == Pattern.STEP) {
            return new StepPresetDto(
                    preset.getId(),
                    preset.getWatering_time(),
                    preset.getPattern(),
                    preset.getSteps(),
                    preset.getDelay()
            );
        }

        return new ContinuousPresetDto(
                preset.getId(),
                preset.getWatering_time(),
                preset.getPattern()
        );
    }
}
