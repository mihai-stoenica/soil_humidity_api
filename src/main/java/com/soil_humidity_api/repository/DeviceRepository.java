package com.soil_humidity_api.repository;

import com.soil_humidity_api.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByApiKey(String apiKey);
    boolean existsByApiKey(String apiKey);
    boolean existsByIdAndUserEmail(Long id, String email);
    Optional<Device> findById(Long id);
}
