package dev.memocode.local_farmfarm_server.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeasurementType {
    TEMPERATURE("온도"),
    HUMIDITY("습도"),
    ;

    private final String name;
}
