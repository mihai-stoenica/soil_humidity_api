package com.soil_humidity_api.controller;

import com.soil_humidity_api.entity.Plant;
import com.soil_humidity_api.repository.PlantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantRepository plantRepository;

    public PlantController(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @GetMapping("/")
    public String home() {
        return "Hello Docker World";
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {

        return ResponseEntity.ok("server running");
    }

    @GetMapping("/plants")
    public List<Plant> getAll() {
        return plantRepository.findAll();
    }

    @PostMapping("/plants")
    public Plant addPlant(@RequestBody Plant plant) {
        return plantRepository.save(plant);
    }

}
