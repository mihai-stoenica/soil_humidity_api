package com.soil_humidity_api.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.soil_humidity_api.enums.Pattern;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "pattern",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ContinuousPresetDtoRes.class, name = "continuous"),
        @JsonSubTypes.Type(value = StepPresetDtoRes.class, name = "step")
})
public sealed interface PresetResponse permits ContinuousPresetDtoRes, StepPresetDtoRes {
    Long id();
    Integer watering_time();
    Pattern pattern();
}
