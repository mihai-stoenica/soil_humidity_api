package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.request.PresetDto;
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

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody PresetDto request, @PathVariable("id") Preset preset) {
        if(preset == null) {
            return ResponseEntity.notFound().build();
        }

        preset.setWatering_time(request.watering_time());
        presetRepository.save(preset);

        PresetDto response = new PresetDto(preset.getWatering_time());

        return ResponseEntity.ok(response);
    }
}
