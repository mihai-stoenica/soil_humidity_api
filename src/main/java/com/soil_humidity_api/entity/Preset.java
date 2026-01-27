package com.soil_humidity_api.entity;

import com.soil_humidity_api.enums.Pattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Preset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

}
