package com.soil_humidity_api.repository;

import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.Humidity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface HumidityRepository extends JpaRepository<Humidity, Long> {
    Optional<Humidity> findById(Long id);
    Page<Humidity> findByDevice(Device device, Pageable pageable);
}
