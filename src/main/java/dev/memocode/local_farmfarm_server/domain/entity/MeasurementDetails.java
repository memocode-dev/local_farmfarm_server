package dev.memocode.local_farmfarm_server.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDetails {
    private MeasurementType measurementType;
    private MeasurementUnit measurementUnit;
}
