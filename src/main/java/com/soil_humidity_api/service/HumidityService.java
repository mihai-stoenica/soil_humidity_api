package com.soil_humidity_api.service;

import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.Humidity;
import com.soil_humidity_api.repository.HumidityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
@AllArgsConstructor
public class HumidityService {
    final HumidityRepository humidityRepository;

    public Page<Humidity> getHumidityData(Device device, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return humidityRepository.findByDevice(device, pageRequest);
    }
}
