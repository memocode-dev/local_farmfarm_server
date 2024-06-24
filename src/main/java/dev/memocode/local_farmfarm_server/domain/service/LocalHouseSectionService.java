package dev.memocode.local_farmfarm_server.domain.service;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouse;
import dev.memocode.local_farmfarm_server.domain.entity.LocalHouseSection;
import dev.memocode.local_farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.local_farmfarm_server.domain.repository.LocalHouseRepository;
import dev.memocode.local_farmfarm_server.domain.repository.LocalHouseSectionRepository;
import dev.memocode.local_farmfarm_server.domain.service.converter.LocalHouseSectionConverter;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseSectionRequest;
import dev.memocode.local_farmfarm_server.mqtt.config.MqttSender;
import dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.memocode.local_farmfarm_server.domain.exception.LocalHouseErrorCode.NOT_FOUND_LOCAL_HOUSE;
import static dev.memocode.local_farmfarm_server.domain.exception.LocalHouseSectionErrorCode.NOT_FOUND_LOCAL_HOUSE_SECTION;
import static dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class LocalHouseSectionService {
    private final LocalHouseRepository localHouseRepository;

    private final LocalHouseSectionRepository localHouseSectionRepository;

    private final LocalHouseSectionConverter localHouseSectionConverter;

    private final MqttSender mqttSender;

    @Transactional
    public void upsertLocalHouseSection(@Valid UpsertLocalHouseSectionRequest request) {
        Optional<LocalHouseSection> _localHouseSection =
                localHouseSectionRepository.findByHouseSectionId(request.getHouseSectionId());

        if (_localHouseSection.isPresent()) {
            LocalHouseSection localHouseSection = _localHouseSection.get();
            updateLocalHouseSectionIfNecessary(localHouseSection, request);
        } else {
            createLocalHouseSection(request);
        }
    }

    @Transactional
    public void syncLocalHouseSection(UUID houseSectionId) {
        LocalHouseSection localHouseSection = localHouseSectionRepository.findByHouseSectionId(houseSectionId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LOCAL_HOUSE_SECTION));

        UpsertLocalHouseSectionRequest request =
                localHouseSectionConverter.toUpsertLocalHouseSectionRequest(localHouseSection);

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouseSections")
                .data(request)
                .build();

        mqttSender.send("response/%s".formatted(localHouseSection.getLocalHouse().getHouseId().toString()), message);
    }

    @Transactional
    public void syncLocalHouseSections() {
        List<LocalHouseSection> localHouseSections = localHouseSectionRepository.findAll();

        for (LocalHouseSection localHouseSection : localHouseSections) {
            syncLocalHouseSection(localHouseSection.getHouseSectionId());
        }
    }

    private LocalHouseSection updateLocalHouseSectionIfNecessary(
            LocalHouseSection localHouseSection, UpsertLocalHouseSectionRequest request) {
        // 하우스 버전이 db에 저장된 하우스 버전보다 클 경우에만 변경
        if (localHouseSection.getHouseSectionVersion() <= request.getHouseSectionVersion()) {
            localHouseSection.changeSectionNumber(request.getSectionNumber());
            localHouseSection.changeHouseSectionVersion(request.getHouseSectionVersion());
            localHouseSection.changeCreatedAt(request.getCreatedAt());
            localHouseSection.changeUpdatedAt(request.getUpdatedAt());
            localHouseSection.changeDeleted(request.getDeleted());
            localHouseSection.changeDeletedAt(request.getDeletedAt());
        } else {
            // 변경하지 않음, 필요시 로깅
            log.info("No changes made to LocalHouse with houseId: {}", request.getHouseId());
        }

        return localHouseSection;
    }

    private LocalHouseSection createLocalHouseSection(UpsertLocalHouseSectionRequest request) {
        LocalHouse localHouse = localHouseRepository.findByHouseId(request.getHouseId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LOCAL_HOUSE));

        LocalHouseSection localHouseSection = LocalHouseSection.builder()
                .sectionNumber(request.getSectionNumber())
                .localHouse(localHouse)
                .houseSectionId(request.getHouseSectionId())
                .houseSectionVersion(request.getHouseSectionVersion())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .deleted(request.getDeleted())
                .deletedAt(request.getDeletedAt())
                .build();

        return localHouseSectionRepository.save(localHouseSection);
    }
}
