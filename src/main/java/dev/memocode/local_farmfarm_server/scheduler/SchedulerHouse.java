package dev.memocode.local_farmfarm_server.scheduler;

import dev.memocode.local_farmfarm_server.domain.entity.LocalHouseSectionSensor;
import dev.memocode.local_farmfarm_server.domain.entity.MeasurementType;
import dev.memocode.local_farmfarm_server.domain.entity.SensorModel;
import dev.memocode.local_farmfarm_server.domain.entity.SensorModelInfo;
import dev.memocode.local_farmfarm_server.domain.repository.LocalHouseSectionSensorRepository;
import dev.memocode.local_farmfarm_server.domain.service.*;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseSectionSensorMeasurementRequest;
import dev.memocode.local_farmfarm_server.modbus.ModbusHandler;
import dev.memocode.local_farmfarm_server.mqtt.config.MqttSender;
import dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerHouse {
    private final LocalHouseService localHouseService;

    private final LocalHouseSectionService localHouseSectionService;

    private final LocalHouseSectionSensorService localHouseSectionSensorService;

    private final LocalHouseSectionSensorRepository localHouseSectionSensorRepository;

    private final XY_MD02ModbusSensorService xyMd02SensorService;

    private final DS18B20SensorService ds18B20SensorService;

    private final MqttSender mqttSender;

    @Scheduled(fixedDelay = 300000)
    public void scheduleHouse() throws InterruptedException {
        log.info("do scheduling local greenhouse");

        localHouseService.syncLocalHouses();

        Thread.sleep(10000);

        localHouseSectionService.syncLocalHouseSections();

        Thread.sleep(10000);

        localHouseSectionSensorService.syncLocalHouseSectionSensors();
    }

    @Transactional(readOnly = true)
    @Scheduled(fixedDelay = 300000)
    public void scheduleMeasurement() throws Exception {
        List<LocalHouseSectionSensor> localHouseSectionSensors = localHouseSectionSensorRepository.findAllByDeleted(false);

        for (LocalHouseSectionSensor localHouseSectionSensor : localHouseSectionSensors) {
            SensorModel sensorModel = localHouseSectionSensor.getSensorModel();
            SensorModelInfo modelInfo = sensorModel.getModelInfo();

            Map<MeasurementType, Float> data = switch (sensorModel) {
                case XY_MD02 -> {
                    ModbusHandler.ModbusSerialMasterWrapper modbusSerialMasterWrapper =
                            ModbusHandler.ModbusSerialMasterWrapper.create(
                                    localHouseSectionSensor.getPortName(), modelInfo.getBaudRate(),
                                    modelInfo.getDataBits(), modelInfo.getParity(), modelInfo.getStopBits(),
                                    modelInfo.getEncoding(), modelInfo.isRs485Mode());

                    yield xyMd02SensorService.getData(modbusSerialMasterWrapper);
                }
                case DS18B20 -> ds18B20SensorService.getData(localHouseSectionSensor.getPortName());
            };

            modelInfo.getMeasurementDetails().forEach(measurementDetail -> {
                UpsertLocalHouseSectionSensorMeasurementRequest request =
                        UpsertLocalHouseSectionSensorMeasurementRequest.builder()
                                .houseId(localHouseSectionSensor.getLocalHouseSection().getLocalHouse().getHouseId())
                                .houseSectionId(localHouseSectionSensor.getLocalHouseSection().getHouseSectionId())
                                .houseSectionSensorId(localHouseSectionSensor.getHouseSectionSensorId())
                                .value(data.get(measurementDetail.getMeasurementType()))
                                .measurementType(measurementDetail.getMeasurementType())
                                .measuredAt(Instant.now())
                                .build();

                Mqtt5Message message = Mqtt5Message.builder()
                        .method(UPSERT)
                        .uri("/localHouseSectionSensorMeasurements")
                        .data(request)
                        .build();

                mqttSender.send("response/%s".formatted(request.getHouseId().toString()), message);
            });
        }

    }
}
