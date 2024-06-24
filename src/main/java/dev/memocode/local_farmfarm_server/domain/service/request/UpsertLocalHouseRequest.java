package dev.memocode.local_farmfarm_server.domain.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpsertLocalHouseRequest {
    @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be not null")
    private UUID houseId;

    @NotBlank(message = "LOCAL_HOUSE_NAME_NOT_BLACK:house name cannot be not null")
    private String name;

    @NotNull(message = "HOUSE_VERSION_NOT_NULL:house version cannot be not null")
    private Long houseVersion;

    @NotNull(message = "HOUSE_CREATED_AT_NOT_NULL:localHouse createdAt cannot be not null")
    private Instant createdAt;

    @NotNull(message = "HOUSE_UPDATED_AT_NOT_NULL:localHouse updatedAt cannot be not null")
    private Instant updatedAt;

    @NotNull(message = "HOUSE_DELETED_NOT_NULL:localHouse deleted cannot be not null")
    private Boolean deleted;

    private Instant deletedAt;
}
