package com.soil_hunidity_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Hello Docker World";
    }

    @GetMapping("/status")
    public String status() {
        return "API is running!";
    }
}
