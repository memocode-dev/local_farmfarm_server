package dev.memocode.local_farmfarm_server.domain.service.request;

import dev.memocode.local_farmfarm_server.domain.entity.MeasurementType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpsertLocalHouseSectionSensorMeasurementRequest {
    @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be not null")
    private UUID houseId;

    @NotNull(message = "HOUSE_SECTION_ID_NOT_NULL:house id cannot be not null")
    private UUID houseSectionId;

    @NotNull(message = "HOUSE_SECTION_SENSOR_ID_NOT_NULL:house id cannot be not null")
    private UUID houseSectionSensorId;

    @NotNull(message = "HOUSE_SECTION_SENSOR_MEASUREMENT_VALUE_NOT_NULL:house name cannot be not null")
    private Float value;

    @NotNull(message = "HOUSE_SECTION_SENSOR_MEASUREMENT_MEASUREMENT_TYPE_NOT_NULL:house name cannot be not null")
    private MeasurementType measurementType;

    @NotNull(message = "HOUSE_CREATED_AT_NOT_NULL:localHouse createdAt cannot be not null")
    private Instant measuredAt;
}
