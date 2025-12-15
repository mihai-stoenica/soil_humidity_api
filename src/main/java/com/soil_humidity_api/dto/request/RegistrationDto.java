package com.soil_humidity_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegistrationDto(
        @NotBlank(message = "Name should not be empty")
        @NotNull
        String name,
        @Email
        String email,
        @Length(min = 5, message = "The password has to be at least 5 characters long")
        String password) {}
