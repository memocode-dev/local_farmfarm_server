package dev.memocode.local_farmfarm_server.domain.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpsertLocalHouseSectionRequest {
    @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be not null")
    private UUID houseId;

    @NotNull(message = "HOUSE_SECTION_ID_NOT_NULL:house id cannot be not null")
    private UUID houseSectionId;

    @NotNull(message = "HOUSE_SECTION_NUMBER_NOT_NULL:house name cannot be not null")
    private Integer sectionNumber;

    @NotNull(message = "HOUSE_SECTION_VERSION_NOT_NULL:house version cannot be not null")
    private Long houseSectionVersion;

    @NotNull(message = "HOUSE_CREATED_AT_NOT_NULL:localHouse createdAt cannot be not null")
    private Instant createdAt;

    @NotNull(message = "HOUSE_UPDATED_AT_NOT_NULL:localHouse updatedAt cannot be not null")
    private Instant updatedAt;

    @NotNull(message = "HOUSE_DELETED_NOT_NULL:localHouse deleted cannot be not null")
    private Boolean deleted;

    private Instant deletedAt;
}
