package dev.memocode.local_farmfarm_server.domain.entity;

import dev.memocode.local_farmfarm_server.domain.base_entity.UUIDAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "local_house_sections")
@EqualsAndHashCode(callSuper = true)
public class LocalHouseSection extends UUIDAbstractEntity {
    @Column(name = "section_number")
    private Integer sectionNumber;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "local_house_id")
    private LocalHouse localHouse;

    @Column(name = "house_section_id", nullable = false)
    private UUID houseSectionId;

    @Column(name = "house_section_version", nullable = false)
    private Long houseSectionVersion;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void changeSectionNumber(Integer sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public void changeHouseSectionVersion(Long houseSectionVersion) {
        this.houseSectionVersion = houseSectionVersion;
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
