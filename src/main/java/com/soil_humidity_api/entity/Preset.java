package com.soil_humidity_api.entity;

import com.soil_humidity_api.enums.Pattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Preset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Length(max = 100, min = 1)
    private String name;

    @NonNull
    private Integer watering_time;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Pattern pattern = Pattern.CONTINUOUS;

    private Integer steps;
    private Integer delay;

    @OneToMany(mappedBy = "preset", cascade = CascadeType.DETACH, orphanRemoval = false)
    private List<WateringEvent> wateringEvents = new ArrayList<>();

}
