package dev.memocode.local_farmfarm_server.domain.service;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouseSection;
import dev.memocode.local_farmfarm_server.domain.entity.LocalHouseSectionSensor;
import dev.memocode.local_farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.local_farmfarm_server.domain.repository.LocalHouseSectionRepository;
import dev.memocode.local_farmfarm_server.domain.repository.LocalHouseSectionSensorRepository;
import dev.memocode.local_farmfarm_server.domain.service.converter.LocalHouseSectionSensorConverter;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseSectionSensorRequest;
import dev.memocode.local_farmfarm_server.mqtt.config.MqttSender;
import dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.memocode.local_farmfarm_server.domain.exception.LocalHouseSectionErrorCode.NOT_FOUND_LOCAL_HOUSE_SECTION;
import static dev.memocode.local_farmfarm_server.domain.exception.LocalHouseSectionSensorErrorCode.NOT_FOUND_LOCAL_HOUSE_SECTION_SENSOR;
import static dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalHouseSectionSensorService {
    private final LocalHouseSectionRepository localHouseSectionRepository;

    private final LocalHouseSectionSensorRepository localHouseSectionSensorRepository;

    private final LocalHouseSectionSensorConverter localHouseSectionSensorConverter;

    private final MqttSender mqttSender;

    @Value("${spring.profiles.active}")
    private String profile;

    @Transactional
    public void upsertLocalHouseSectionSensor(@Valid UpsertLocalHouseSectionSensorRequest request) {
        Optional<LocalHouseSectionSensor> _localHouseSectionSensor =
                localHouseSectionSensorRepository.findByHouseSectionSensorId(request.getHouseSectionSensorId());

        if (_localHouseSectionSensor.isPresent()) {
            LocalHouseSectionSensor localHouseSectionSensor = _localHouseSectionSensor.get();
            updateLocalHouseSectionSensorIfNecessary(localHouseSectionSensor, request);
        } else {
            createLocalHouseSectionSensor(request);
        }
    }

    @Transactional
    public void syncLocalHouseSectionSensor(UUID houseSectionSensorId) {
        LocalHouseSectionSensor localHouseSectionSensor = localHouseSectionSensorRepository.findByHouseSectionSensorId(houseSectionSensorId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LOCAL_HOUSE_SECTION_SENSOR));

        UpsertLocalHouseSectionSensorRequest request =
                localHouseSectionSensorConverter.toUpsertLocalHouseSectionSensorRequest(localHouseSectionSensor);

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouseSectionSensors")
                .data(request)
                .build();

        String topic = profile.equals("prod") ? "prod/response/%s" : "dev/response/%s";
        mqttSender.send(topic.formatted(request.getHouseId().toString()), message);
    }

    @Transactional
    public void syncLocalHouseSectionSensors() {
        List<LocalHouseSectionSensor> localHouseSectionSensors = localHouseSectionSensorRepository.findAll();

        for (LocalHouseSectionSensor localHouseSectionSensor : localHouseSectionSensors) {
            syncLocalHouseSectionSensor(localHouseSectionSensor.getHouseSectionSensorId());
        }
    }

    private LocalHouseSectionSensor updateLocalHouseSectionSensorIfNecessary(
            LocalHouseSectionSensor localHouseSectionSensor, UpsertLocalHouseSectionSensorRequest request) {
        // 하우스 버전이 db에 저장된 하우스 버전보다 클 경우에만 변경
        if (localHouseSectionSensor.getHouseSectionSensorVersion() <= request.getHouseSectionSensorVersion()) {
            LocalHouseSection localHouseSection = localHouseSectionRepository.findByHouseSectionId(request.getHouseSectionId())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_LOCAL_HOUSE_SECTION));

            localHouseSectionSensor.changeNameForAdmin(request.getNameForAdmin());
            localHouseSectionSensor.changeNameForUser(request.getNameForUser());
            localHouseSectionSensor.changeHouseSectionSensorVersion(request.getHouseSectionSensorVersion());
            localHouseSectionSensor.changeLocalHouseSection(localHouseSection);
            localHouseSectionSensor.changePortName(request.getPortName());
            localHouseSectionSensor.changeCreatedAt(request.getCreatedAt());
            localHouseSectionSensor.changeUpdatedAt(request.getUpdatedAt());
            localHouseSectionSensor.changeDeleted(request.getDeleted());
            localHouseSectionSensor.changeDeletedAt(request.getDeletedAt());
        } else {
            // 변경하지 않음, 필요시 로깅
            log.info("No changes made to LocalHouse with houseId: {}", request.getHouseId());
        }

        return localHouseSectionSensor;
    }

    private LocalHouseSectionSensor createLocalHouseSectionSensor(UpsertLocalHouseSectionSensorRequest request) {
        LocalHouseSection localHouseSection = localHouseSectionRepository.findByHouseSectionId(request.getHouseSectionId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LOCAL_HOUSE_SECTION));

        LocalHouseSectionSensor localHouseSectionSensor = LocalHouseSectionSensor.builder()
                .houseSectionSensorId(request.getHouseSectionSensorId())
                .houseSectionSensorVersion(request.getHouseSectionSensorVersion())
                .localHouseSection(localHouseSection)
                .nameForAdmin(request.getNameForAdmin())
                .nameForUser(request.getNameForUser())
                .sensorModel(request.getSensorModel())
                .portName(request.getPortName())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .deleted(request.getDeleted())
                .deletedAt(request.getDeletedAt())
                .build();

        return localHouseSectionSensorRepository.save(localHouseSectionSensor);
    }
}
