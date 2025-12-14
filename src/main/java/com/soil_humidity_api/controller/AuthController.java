package com.soil_humidity_api.controller;

import com.soil_humidity_api.dto.LoginDto;
import com.soil_humidity_api.dto.RegistrationDto;
import com.soil_humidity_api.entity.User;
import com.soil_humidity_api.repository.UserRepository;
import com.soil_humidity_api.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDto request) {
        if(userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(Map.of("message","This email already exists"));
        }

        String hashedPwd = passwordEncoder.encode(request.password());

        assert hashedPwd != null;
        User newUser = new User(request.name(),request.email(), hashedPwd);

        userRepository.save(newUser);

        return ResponseEntity.ok().body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginRequest) {

        Optional<User> existingUser = userRepository.findByEmail(loginRequest.email());

        if(existingUser.isEmpty() || !passwordEncoder.matches(loginRequest.password(), existingUser.get().getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Wrong credentials"));
        }

        String token = jwtService.generateToken(existingUser.get().getEmail());

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(6*60*60)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(existingUser.get());
    }

    @PostMapping("/logout")
    public  ResponseEntity<?> logout() {
        ResponseCookie cleanCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                .body(Map.of("message","You have been logged out."));
    }
}
