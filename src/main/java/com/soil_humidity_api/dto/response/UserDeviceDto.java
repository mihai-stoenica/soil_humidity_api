package com.soil_humidity_api.dto.response;

import java.util.List;

public record UserDeviceDto(
        Long id,
        String name,
        List<DeviceDto> devices
) {}