package dev.memocode.local_farmfarm_server.domain.repository;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouseSectionSensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocalHouseSectionSensorRepository extends JpaRepository<LocalHouseSectionSensor, UUID> {
    Optional<LocalHouseSectionSensor> findByHouseSectionSensorId(UUID houseSectionSensorId);
    List<LocalHouseSectionSensor> findAllByDeleted(boolean deleted);
}
