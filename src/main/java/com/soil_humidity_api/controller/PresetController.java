package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.request.ContinuousPresetDto;
import com.soil_humidity_api.dto.request.PresetRequest;
import com.soil_humidity_api.dto.request.StepPresetDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.Preset;
import com.soil_humidity_api.repository.PresetRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/presets")
@RequiredArgsConstructor
public class PresetController {
    private final PresetRepository presetRepository;

    @PostMapping("/{deviceId}")
    public ResponseEntity<?> update(@Valid @RequestBody PresetRequest request, @PathVariable("deviceId") Device device) {
        if(device == null) {
            return ResponseEntity.notFound().build();
        }

        Preset preset = device.getPreset();

        if(preset == null) {
            return ResponseEntity.notFound().build();
        }

        preset.setWatering_time(request.watering_time());
        preset.setPattern(request.pattern());

        if (request instanceof StepPresetDto stepRequest) {
            preset.setSteps(stepRequest.steps());
            preset.setDelay(stepRequest.delay());

        } else if (request instanceof ContinuousPresetDto) {
            preset.setSteps(null);
            preset.setDelay(null);
        }

        presetRepository.save(preset);

        StepPresetDto response =  new StepPresetDto(preset.getWatering_time(), preset.getPattern(), preset.getSteps(), preset.getDelay());

        return ResponseEntity.ok(response);
    }
}
