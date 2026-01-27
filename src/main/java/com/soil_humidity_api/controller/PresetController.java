package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.request.ContinuousPresetDto;
import com.soil_humidity_api.dto.request.PresetRequest;
import com.soil_humidity_api.dto.request.StepPresetDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.Preset;
import com.soil_humidity_api.mapper.PresetMapper;
import com.soil_humidity_api.repository.DeviceRepository;
import com.soil_humidity_api.repository.PresetRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/presets")
@RequiredArgsConstructor
public class PresetController {
    private final PresetRepository presetRepository;
    private final PresetMapper presetMapper;
    private final DeviceRepository deviceRepository;

    @PostMapping("/{deviceId}")
    public ResponseEntity<?> add(@Valid @RequestBody PresetRequest request, @PathVariable("deviceId") Device device) {
        if(device == null) {
            return ResponseEntity.notFound().build();
        }

        Preset preset = new Preset();

        preset.setWatering_time(request.watering_time());
        preset.setPattern(request.pattern());

        if (request instanceof StepPresetDto stepRequest) {
            preset.setSteps(stepRequest.steps());
            preset.setDelay(stepRequest.delay());

        } else if (request instanceof ContinuousPresetDto) {
            preset.setSteps(null);
            preset.setDelay(null);
        }

        device.addPreset(preset);

        presetRepository.save(preset);

        PresetRequest response =  presetMapper.toDto(preset);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<?> getAll(@PathVariable("deviceId") Device device) {
        if(device == null) {
            return ResponseEntity.notFound().build();
        }

        List<PresetRequest> response = device.getPresets()
                .stream()
                .map(presetMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/device/{deviceId}/preset/{presetId}")
    public ResponseEntity<?> delete(@PathVariable("deviceId") Device device, @PathVariable("presetId") Preset preset) {
        if(device == null || preset == null) {
            return ResponseEntity.notFound().build();
        }

        if (!preset.getDevice().getId().equals(device.getId())) {
           return ResponseEntity.badRequest().body(Map.of("message", "Preset does not belong to this device!"));
        }

        if (preset.equals(device.getActivePreset())) {
            return ResponseEntity.badRequest().body(Map.of("message", "You cannot delete the active preset!"));
        }

        device.removePreset(preset);
        presetRepository.delete(preset);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/device/{deviceId}/preset/{presetId}")
    public ResponseEntity<?> setActive(@PathVariable("deviceId") Device device, @PathVariable("presetId") Preset preset) {
        if(device == null || preset == null) {
            return ResponseEntity.notFound().build();
        }

        if (!preset.getDevice().getId().equals(device.getId())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Preset does not belong to this device!"));
        }

        device.setActivePreset(preset);
        deviceRepository.save(device);

        PresetRequest response =  presetMapper.toDto(preset);

        return ResponseEntity.ok().body(response);
    }
}
