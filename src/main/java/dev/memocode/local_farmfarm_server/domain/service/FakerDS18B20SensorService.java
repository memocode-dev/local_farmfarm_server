package dev.memocode.local_farmfarm_server.domain.service;

import dev.memocode.local_farmfarm_server.domain.entity.MeasurementType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Profile("!prod")
public class FakerDS18B20SensorService implements DS18B20SensorService {
    private final Random random = new Random();

    @Override
    public Map<MeasurementType, Float> getData(String portName) throws Exception {
        // 온도 센서 데이터 랜덤 생성 (10.0도에서 40.0도 사이)
        float temperature = 10.0f + random.nextFloat() * 30.0f; // 10.0도에서 40.0도 사이의 랜덤 값 생성
        Map<MeasurementType, Float> data = new HashMap<>();
        data.put(MeasurementType.TEMPERATURE, temperature);
        return data;
    }
}
