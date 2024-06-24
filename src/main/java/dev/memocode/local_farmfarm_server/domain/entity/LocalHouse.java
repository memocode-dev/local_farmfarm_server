package dev.memocode.local_farmfarm_server.domain.entity;

import dev.memocode.local_farmfarm_server.domain.base_entity.UUIDAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "local_houses")
@EqualsAndHashCode(callSuper = true)
public class LocalHouse extends UUIDAbstractEntity {
    @Column(name = "name")
    private String name;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "house_id", nullable = false)
    private UUID houseId;

    @Column(name = "house_version", nullable = false)
    private Long houseVersion;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeHouseVersion(Long houseVersion) {
        this.houseVersion = houseVersion;
    }

    public void changeCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void changeUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void changeDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void changeDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
