package com.soil_humidity_api.repository;

import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface HumidityRepository extends JpaRepository<Record, Long> {
    Optional<Record> findById(Long id);
    Page<Record> findByDevice(Device device, Pageable pageable);
}
