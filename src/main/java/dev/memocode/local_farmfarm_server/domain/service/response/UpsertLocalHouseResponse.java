package dev.memocode.local_farmfarm_server.domain.service.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpsertLocalHouseResponse {
    private UUID houseId;
    private String name;
    private Long houseVersion;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean deleted;
    private Instant deletedAt;
}
