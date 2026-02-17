package com.soil_humidity_api.mapper;

import com.soil_humidity_api.config.DeviceSessionRegistry;
import com.soil_humidity_api.dto.response.DeviceDto;
import com.soil_humidity_api.entity.Device;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeviceMapper {
    private final DeviceSessionRegistry deviceSessionRegistry;

    public DeviceDto toDto(Device device) {
        return new DeviceDto(
                device.getId(),
                device.getName(),
                deviceSessionRegistry.isDeviceConnected(device.getId()),
                device.getLastSeen(),
                device.getLastHumidity(),
                device.getActivePreset().getId()
        );
    }
}
