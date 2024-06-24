package dev.memocode.local_farmfarm_server.domain.repository;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocalHouseRepository extends JpaRepository<LocalHouse, UUID> {
    Optional<LocalHouse> findByHouseId(UUID houseId);
}
