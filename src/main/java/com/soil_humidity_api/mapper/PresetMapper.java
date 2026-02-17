package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.response.ContinuousPresetDtoRes;
import com.soil_humidity_api.dto.response.PresetResponse;
import com.soil_humidity_api.dto.response.StepPresetDtoRes;
import com.soil_humidity_api.entity.Preset;
import com.soil_humidity_api.enums.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PresetMapper {
    public PresetResponse toDto(Preset preset) {
        if (preset.getPattern() == Pattern.STEP) {
            return new StepPresetDtoRes(
                    preset.getId(),
                    preset.getWatering_time(),
                    preset.getPattern(),
                    preset.getSteps(),
                    preset.getDelay()
            );
        }

        return new ContinuousPresetDtoRes(
                preset.getId(),
                preset.getWatering_time(),
                preset.getPattern()
        );
    }
}
