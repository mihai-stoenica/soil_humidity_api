package com.soil_humidity_api.dto.request;

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
        @JsonSubTypes.Type(value = ContinuousPresetDto.class, name = "continuous"),
        @JsonSubTypes.Type(value = StepPresetDto.class, name = "step")
})
public sealed interface PresetRequest permits ContinuousPresetDto, StepPresetDto {
    Integer watering_time();
    Pattern pattern();
    String name();
}
