package dev.memocode.local_farmfarm_server.domain.service;

import dev.memocode.local_farmfarm_server.domain.entity.MeasurementType;
import dev.memocode.local_farmfarm_server.modbus.ModbusHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Profile("!prod")
public class FakerXY_MD02ModbusSensorService implements XY_MD02ModbusSensorService {
    private final Random random = new Random();

    @Override
    public Map<MeasurementType, Float> getData(ModbusHandler.ModbusSerialMasterWrapper modbusSerialMasterWrapper) throws Exception {
        // 온도와 습도 센서 데이터 랜덤 생성
        float temperature = 10.0f + random.nextFloat() * 30.0f; // 10.0도에서 40.0도 사이의 랜덤 값 생성
        float humidity = 30.0f + random.nextFloat() * 70.0f; // 30.0%에서 100.0% 사이의 랜덤 값 생성

        Map<MeasurementType, Float> data = new HashMap<>();
        data.put(MeasurementType.TEMPERATURE, temperature);
        data.put(MeasurementType.HUMIDITY, humidity);

        return data;
    }
}
