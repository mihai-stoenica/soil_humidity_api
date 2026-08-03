package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.request.RecordRequestDto;
import com.soil_humidity_api.dto.response.RecordResponseDto;
import com.soil_humidity_api.dto.response.SingleRecordResponseDto;
import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.entity.Record;
import com.soil_humidity_api.mapper.RecordMapper;
import com.soil_humidity_api.mapper.SingleRecordMapper;
import com.soil_humidity_api.repository.DeviceRepository;
import com.soil_humidity_api.repository.HumidityRepository;
import com.soil_humidity_api.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/humidity")
@RequiredArgsConstructor
public class HumidityController {
    private final HumidityRepository humidityRepository;
    private final RecordService recordService;
    private final DeviceRepository deviceRepository;
    private final SingleRecordMapper singleRecordMapper;
    private final RecordMapper recordMapper;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody RecordRequestDto request, HttpServletRequest r) {
        String apiKey = r.getHeader("X-API-KEY");

        Optional<Device> optionalDevice = deviceRepository.findByApiKey(apiKey);

        if(optionalDevice.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Device device = optionalDevice.get();

        Record record = new Record();

        record.setDevice(device);
        record.setHumidity(request.humidity());
        record.setTemperature(request.temperature());
        record.setLight_level(request.light_level());

        humidityRepository.save(record);

        SingleRecordResponseDto response = singleRecordMapper.toDto(record);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/history/device/{deviceId}")
    @PreAuthorize("hasPermission(#device, 'READ')")
    public ResponseEntity<?> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("deviceId") Device device
    ) {
        if(device == null) {
            return ResponseEntity.notFound().build();
        }
        Page<Record> resultPage = recordService.getHumidityData(device, page, size);

        List<SingleRecordResponseDto> records = resultPage.getContent()
                .stream()
                .map(singleRecordMapper::toDto)
                .toList();

        RecordResponseDto response = recordMapper.toDto(records, resultPage.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
