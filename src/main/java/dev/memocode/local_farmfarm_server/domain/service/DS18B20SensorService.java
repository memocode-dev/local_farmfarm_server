package dev.memocode.local_farmfarm_server.domain.service;

import dev.memocode.local_farmfarm_server.domain.entity.MeasurementType;

import java.util.Map;

public interface DS18B20SensorService {
    Map<MeasurementType, Float> getData(String portName) throws Exception;
}
