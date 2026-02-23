package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.response.DeviceDto;
import com.soil_humidity_api.entity.Device;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeviceMapper {

    public DeviceDto toDto(Device device) {
        return new DeviceDto(
                device.getId(),
                device.getName(),
                device.isConnected(),
                device.getLastSeen(),
                device.getLastHumidity(),
                device.getLastTemperature(),
                device.getActivePreset().getId()
        );
    }
}
