package dev.memocode.local_farmfarm_server.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static dev.memocode.local_farmfarm_server.domain.entity.MeasurementType.HUMIDITY;
import static dev.memocode.local_farmfarm_server.domain.entity.MeasurementType.TEMPERATURE;
import static dev.memocode.local_farmfarm_server.domain.entity.MeasurementUnit.CELSIUS;
import static dev.memocode.local_farmfarm_server.domain.entity.MeasurementUnit.PERCENT;
import static dev.memocode.local_farmfarm_server.domain.entity.SensorModelCommunicationType.ONE_WIRE;
import static dev.memocode.local_farmfarm_server.domain.entity.SensorModelCommunicationType.RS485;

@Getter
@AllArgsConstructor
public enum SensorModel {
    XY_MD02(SensorModelInfo.builder()
            .name("XY_MD02")
            .description("온습도 센서")
            .measurementDetails(
                    List.of(new MeasurementDetails(TEMPERATURE, CELSIUS), new MeasurementDetails(HUMIDITY, PERCENT)))
            .communicationType(RS485)
            .baudRate(9600)
            .dataBits(8)
            .parity("none")
            .stopBits(1)
            .encoding("rtu")
            .rs485Mode(true)
            .build()),
    DS18B20(SensorModelInfo.builder()
            .name("DS18B20")
            .description("온도 센서")
            .measurementDetails(
                    List.of(new MeasurementDetails(TEMPERATURE, CELSIUS)))
            .communicationType(ONE_WIRE)
            .baudRate(0)
            .dataBits(0)
            .parity("not")
            .stopBits(0)
            .encoding("not")
            .rs485Mode(false)
            .build()),
    ;

    private final SensorModelInfo modelInfo;
}
