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

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "local_house_section_sensors")
@EqualsAndHashCode(callSuper = true)
public class LocalHouseSectionSensor extends UUIDAbstractEntity {
    @Column(name = "name_for_admin")
    private String nameForAdmin;

    @Column(name = "name_for_user")
    private String nameForUser;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "local_house_section_id")
    private LocalHouseSection localHouseSection;

    @Column(name = "house_section_sensor_id", nullable = false)
    private UUID houseSectionSensorId;

    @Column(name = "house_section_sensor_version", nullable = false)
    private Long houseSectionSensorVersion;

    @Enumerated(STRING)
    @Column(name = "sensor_model")
    private SensorModel sensorModel;

    @Column(name = "port_name")
    private String portName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void changeNameForAdmin(String nameForAdmin) {
        this.nameForAdmin = nameForAdmin;
    }

    public void changeNameForUser(String nameForUser) {
        this.nameForUser = nameForUser;
    }

    public void changeHouseSectionSensorVersion(Long houseSectionSensorVersion) {
        this.houseSectionSensorVersion = houseSectionSensorVersion;
    }

    public void changeLocalHouseSection(LocalHouseSection localHouseSection) {
        this.localHouseSection = localHouseSection;
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

    public void changePortName(String portName) {
        this.portName = portName;
    }
}
