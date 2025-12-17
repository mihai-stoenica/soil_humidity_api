package com.soil_humidity_api.repository;

import com.soil_humidity_api.entity.Preset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PresetRepository extends JpaRepository<Preset, Long> {
    Optional<Preset> findById(Long id);
}
