package dev.memocode.local_farmfarm_server.domain.service.converter;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouse;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseRequest;
import org.springframework.stereotype.Component;

@Component
public class LocalHouseConverter {
    public UpsertLocalHouseRequest toUpsertLocalHouseRequest(LocalHouse localHouse) {
        return UpsertLocalHouseRequest.builder()
                .houseId(localHouse.getHouseId())
                .name(localHouse.getName())
                .houseVersion(localHouse.getHouseVersion())
                .createdAt(localHouse.getCreatedAt())
                .updatedAt(localHouse.getUpdatedAt())
                .deletedAt(localHouse.getDeletedAt())
                .deleted(localHouse.getDeleted())
                .build();
    }
}
