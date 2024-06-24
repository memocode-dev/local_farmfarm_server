package dev.memocode.local_farmfarm_server.domain.service;

import dev.memocode.local_farmfarm_server.domain.entity.MeasurementType;
import dev.memocode.local_farmfarm_server.modbus.ModbusHandler;

import java.util.Map;

public interface ModbusSensorService {
    Map<MeasurementType, Float> getData(ModbusHandler.ModbusSerialMasterWrapper modbusSerialMasterWrapper) throws Exception;
}
