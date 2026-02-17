package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.request.HumidityRequestDto;
import com.soil_humidity_api.dto.response.HumidityResponseDto;
import com.soil_humidity_api.dto.response.SingleHumidityResponseDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.Humidity;
import com.soil_humidity_api.repository.DeviceRepository;
import com.soil_humidity_api.repository.HumidityRepository;
import com.soil_humidity_api.service.HumidityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/humidity")
@RequiredArgsConstructor
public class HumidityController {
    private final HumidityRepository humidityRepository;
    private final HumidityService humidityService;
    private final DeviceRepository deviceRepository;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody HumidityRequestDto request, HttpServletRequest r) {
        String apiKey = r.getHeader("X-API-KEY");

        Optional<Device> optionalDevice = deviceRepository.findByApiKey(apiKey);

        if(optionalDevice.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Device device = optionalDevice.get();

        Humidity humidity = new Humidity();

        humidity.setDevice(device);
        humidity.setValue(request.value());

        humidityRepository.save(humidity);

        SingleHumidityResponseDto responseDto = new SingleHumidityResponseDto(humidity.getId(), humidity.getValue(), humidity.getTimestamp());

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/history/device/{deviceId}")
    public ResponseEntity<?> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Humidity> resultPage = humidityService.getHumidityData(page, size);

        List<SingleHumidityResponseDto> records = resultPage.getContent()
                .stream()
                .map(humidity -> new SingleHumidityResponseDto(
                        humidity.getId(),
                        humidity.getValue(),
                        humidity.getTimestamp()
                ))
                .toList();

        HumidityResponseDto response = new HumidityResponseDto(
                records,
                resultPage.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }
}
