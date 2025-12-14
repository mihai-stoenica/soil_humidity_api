package com.soil_humidity_api.dto;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record LoginDto(
        @Email
        String email,
        @Length(min = 5, message = "The password has to be at least 5 characters long")
        String password) {}
