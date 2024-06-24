package dev.memocode.local_farmfarm_server.domain.service;

import dev.memocode.local_farmfarm_server.domain.entity.MeasurementType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static dev.memocode.local_farmfarm_server.domain.entity.MeasurementType.TEMPERATURE;

@Service
@Profile({"prod"})
public class ProdDS18B20SensorService implements DS18B20SensorService {
    public Map<MeasurementType, Float> getData(String portName) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(portName))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("t=")) {
                    String tempStr = line.split("t=")[1];
                    Map<MeasurementType, Float> _data = new HashMap<>();
                    double temperature = Double.parseDouble(tempStr) / 1000.0;
                    _data.put(TEMPERATURE, (float) temperature);
                    return _data;
                }
            }
        }

        throw new IOException("Temperature data not found in " + portName);
    }
}
