package com.soil_humidity_api.mapper;

import com.soil_humidity_api.dto.response.UserDto;
import com.soil_humidity_api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
