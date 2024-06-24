package dev.memocode.local_farmfarm_server.domain.service.converter;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouseSection;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseSectionRequest;
import org.springframework.stereotype.Component;

@Component
public class LocalHouseSectionConverter {

    public UpsertLocalHouseSectionRequest toUpsertLocalHouseSectionRequest(LocalHouseSection localHouseSection) {
        return UpsertLocalHouseSectionRequest.builder()
                .houseId(localHouseSection.getLocalHouse().getHouseId())
                .houseSectionId(localHouseSection.getHouseSectionId())
                .sectionNumber(localHouseSection.getSectionNumber())
                .houseSectionVersion(localHouseSection.getHouseSectionVersion())
                .createdAt(localHouseSection.getCreatedAt())
                .updatedAt(localHouseSection.getUpdatedAt())
                .deletedAt(localHouseSection.getDeletedAt())
                .deleted(localHouseSection.getDeleted())
                .build();
    }
}
