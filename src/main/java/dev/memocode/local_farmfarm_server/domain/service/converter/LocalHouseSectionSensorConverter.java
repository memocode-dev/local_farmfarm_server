package dev.memocode.local_farmfarm_server.domain.service.converter;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouseSectionSensor;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseSectionSensorRequest;
import org.springframework.stereotype.Component;

@Component
public class LocalHouseSectionSensorConverter {
    public UpsertLocalHouseSectionSensorRequest toUpsertLocalHouseSectionSensorRequest(LocalHouseSectionSensor localHouseSectionSensor) {
        return UpsertLocalHouseSectionSensorRequest.builder()
                .houseId(localHouseSectionSensor.getLocalHouseSection().getLocalHouse().getHouseId())
                .houseSectionId(localHouseSectionSensor.getLocalHouseSection().getHouseSectionId())
                .houseSectionSensorId(localHouseSectionSensor.getHouseSectionSensorId())
                .houseSectionSensorVersion(localHouseSectionSensor.getHouseSectionSensorVersion())
                .nameForAdmin(localHouseSectionSensor.getNameForAdmin())
                .nameForUser(localHouseSectionSensor.getNameForUser())
                .sensorModel(localHouseSectionSensor.getSensorModel())
                .portName(localHouseSectionSensor.getPortName())
                .createdAt(localHouseSectionSensor.getCreatedAt())
                .updatedAt(localHouseSectionSensor.getUpdatedAt())
                .deletedAt(localHouseSectionSensor.getDeletedAt())
                .deleted(localHouseSectionSensor.getDeleted())
                .build();
    }
}
