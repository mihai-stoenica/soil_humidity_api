package com.soil_humidity_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
    public String status() {
        return "API is running!";
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
