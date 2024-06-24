package dev.memocode.local_farmfarm_server.domain.entity;

import lombok.Getter;

@Getter
public enum MeasurementUnit {
    CELSIUS("°C"),
    PERCENT("%"),
    ;

    private final String symbol;

    MeasurementUnit(String symbol) {
        this.symbol = symbol;
    }
}
