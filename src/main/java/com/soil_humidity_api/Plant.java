package com.soil_humidity_api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Plant {
    // getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int humidity;

    public Plant() {}

    public Plant(String name, int humidity) {
        this.name = name;
        this.humidity = humidity;
    }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setHumidity(int humidity) { this.humidity = humidity; }
}
