package dev.memocode.local_farmfarm_server.domain.repository;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouseSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocalHouseSectionRepository extends JpaRepository<LocalHouseSection, Long> {
    Optional<LocalHouseSection> findByHouseSectionId(UUID houseSectionId);
}
