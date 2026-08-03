package com.soil_humidity_api.entity;

import com.soil_humidity_api.enums.TriggerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WateringEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @NotNull
    private Device device;

    @ManyToOne
    @JoinColumn(name = "preset_id")
    @NotNull
    private Preset preset;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @NotNull
    private LocalDateTime timestamp;

    @Column(name = "trigger_type", nullable = false)
    @NotNull
    private TriggerType triggerType;
}
