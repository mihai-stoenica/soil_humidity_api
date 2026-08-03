package com.soil_humidity_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @Column(unique = true)
    private String apiKey;

    @NonNull
    private String secret;

    @Column(nullable = false)
    private boolean connected = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<Preset> presets = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "active_preset_id")
    private Preset activePreset;

    @Min(value = 0, message = "Humidity cannot be less than 0%")
    @Max(value = 100, message = "Humidity cannot be more than 100%")
    private Integer lastHumidity = null;

    private Float lastTemperature = null;

    private Instant lastSeen = null;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    @NotNull
    private List<Record> humidities = new ArrayList<>();

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<WateringEvent> wateringEvents = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public void addPreset(Preset preset) {
        presets.add(preset);
        preset.setDevice(this);

        activePreset = preset;
    }

    public void removePreset(Preset preset) {
        presets.remove(preset);
        preset.setDevice(null);
    }

}
