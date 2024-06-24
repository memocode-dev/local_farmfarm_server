package dev.memocode.local_farmfarm_server.domain.service;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouse;
import dev.memocode.local_farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.local_farmfarm_server.domain.repository.LocalHouseRepository;
import dev.memocode.local_farmfarm_server.domain.service.converter.LocalHouseConverter;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseRequest;
import dev.memocode.local_farmfarm_server.mqtt.config.MqttSender;
import dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

import static dev.memocode.local_farmfarm_server.domain.exception.LocalHouseErrorCode.NOT_FOUND_LOCAL_HOUSE;
import static dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class LocalHouseService {
    private final LocalHouseRepository localHouseRepository;

    private final LocalHouseConverter localHouseConverter;

    private final MqttSender mqttSender;

    @Transactional
    public void upsertLocalHouse(@Valid UpsertLocalHouseRequest request) {
        localHouseRepository.findByHouseId(request.getHouseId())
                .map(existingLocalHouse -> updateLocalHouseIfNecessary(existingLocalHouse, request))
                .orElseGet(() -> createLocalHouse(request));
    }

    public void syncLocalHouse(UUID houseId) {
        LocalHouse localHouse = localHouseRepository.findByHouseId(houseId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LOCAL_HOUSE));

        UpsertLocalHouseRequest request = localHouseConverter.toUpsertLocalHouseRequest(localHouse);

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouses")
                .data(request)
                .build();

        mqttSender.sendResponse(localHouse.getHouseId(), message);

    }

    public void syncLocalHouses() {
        List<LocalHouse> localHouses = localHouseRepository.findAll();

        for (LocalHouse localHouse : localHouses) {
            syncLocalHouse(localHouse.getHouseId());
        }
    }

    private LocalHouse updateLocalHouseIfNecessary(LocalHouse localHouse, UpsertLocalHouseRequest request) {
        // 하우스 버전이 db에 저장된 하우스 버전보다 클 경우에만 변경
        if (localHouse.getHouseVersion() <= request.getHouseVersion()) {
            localHouse.changeName(request.getName());
            localHouse.changeHouseVersion(request.getHouseVersion());
            localHouse.changeCreatedAt(request.getCreatedAt());
            localHouse.changeUpdatedAt(request.getUpdatedAt());
            localHouse.changeDeleted(request.getDeleted());
            localHouse.changeDeletedAt(request.getDeletedAt());
        } else {
            // 변경하지 않음, 필요시 로깅
            log.info("No changes made to LocalHouse with houseId: {}", request.getHouseId());
        }

        return localHouse;
    }

    private LocalHouse createLocalHouse(UpsertLocalHouseRequest request) {
        LocalHouse localHouse = LocalHouse.builder()
                .name(request.getName())
                .houseId(request.getHouseId())
                .houseVersion(request.getHouseVersion())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .deleted(request.getDeleted())
                .deletedAt(request.getDeletedAt())
                .build();

        return localHouseRepository.save(localHouse);
    }
}
