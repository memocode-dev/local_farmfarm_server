package dev.memocode.local_farmfarm_server.domain.service.request;

import dev.memocode.local_farmfarm_server.domain.entity.SensorModel;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpsertLocalHouseSectionSensorRequest {
    @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be not null")
    private UUID houseId;

    @NotNull(message = "HOUSE_SECTION_ID_NOT_NULL:house id cannot be not null")
    private UUID houseSectionId;

    @NotNull(message = "HOUSE_SECTION_SENSOR_ID_NOT_NULL:house id cannot be not null")
    private UUID houseSectionSensorId;

    @NotNull(message = "HOUSE_SECTION_SENSOR_NAME_FOR_ADMIN_NOT_NULL:house name cannot be not null")
    private String nameForAdmin;

    @NotNull(message = "HOUSE_SECTION_SENSOR_NAME_FOR_USER_NOT_NULL:house name cannot be not null")
    private String nameForUser;

    @NotNull(message = "HOUSE_SECTION_SENSOR_SENSOR_MODEL_NOT_NULL:house section sensor sensorModel cannot be not null")
    private SensorModel sensorModel;

    @NotNull(message = "HOUSE_SECTION_SENSOR_PORT_NAME_NOT_NULL:house section sensor sensorModel cannot be not null")
    private String portName;

    @NotNull(message = "HOUSE_SECTION_SENSOR_VERSION_NOT_NULL:house version cannot be not null")
    private Long houseSectionSensorVersion;

    @NotNull(message = "HOUSE_CREATED_AT_NOT_NULL:localHouse createdAt cannot be not null")
    private Instant createdAt;

    @NotNull(message = "HOUSE_UPDATED_AT_NOT_NULL:localHouse updatedAt cannot be not null")
    private Instant updatedAt;

    @NotNull(message = "HOUSE_DELETED_NOT_NULL:localHouse deleted cannot be not null")
    private Boolean deleted;

    private Instant deletedAt;
}
