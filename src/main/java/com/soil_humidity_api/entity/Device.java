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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "device", cascade = CascadeType.ALL)
    @NotNull
    private Preset preset;

    @Min(value = 0, message = "Humidity cannot be less than 0%")
    @Max(value = 100, message = "Humidity cannot be more than 100%")
    private Integer lastHumidity = null;

    private Instant lastSeen = null;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    @NotNull
    private List<Humidity> humidities = new ArrayList<>();

}
