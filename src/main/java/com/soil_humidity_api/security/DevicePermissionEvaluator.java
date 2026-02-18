package com.soil_humidity_api.security;

import com.soil_humidity_api.entity.Device;
import com.soil_humidity_api.repository.DeviceRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class DevicePermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public boolean hasPermission(@NonNull Authentication auth, @NonNull Object targetDomainObject, @NonNull Object permission) {
        if(targetDomainObject instanceof Device device) {
            String email = auth.getName();
            return device.getUser().getEmail().equals(email);
        }
        return false;
    }

    @Override
    public boolean hasPermission(@NonNull Authentication auth, @NonNull Serializable targetId, @NonNull String targetType,@NonNull Object permission) {

        if ("Device".equalsIgnoreCase(targetType)) {
            Long deviceId = (Long) targetId;
            String email = auth.getName();

            return deviceRepository.existsByIdAndUserEmail(deviceId,email);
        }

        return false;
    }

}
