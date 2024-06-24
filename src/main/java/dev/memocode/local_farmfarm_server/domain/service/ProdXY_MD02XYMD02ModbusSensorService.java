package dev.memocode.local_farmfarm_server.domain.service;

import com.ghgande.j2mod.modbus.ModbusException;
import dev.memocode.local_farmfarm_server.domain.entity.MeasurementType;
import dev.memocode.local_farmfarm_server.modbus.ModbusHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static dev.memocode.local_farmfarm_server.domain.entity.MeasurementType.HUMIDITY;
import static dev.memocode.local_farmfarm_server.domain.entity.MeasurementType.TEMPERATURE;

@Service
@Profile({"prod"})
@RequiredArgsConstructor
public class ProdXY_MD02XYMD02ModbusSensorService implements XY_MD02ModbusSensorService {
    private final ModbusHandler modbusHandler;

    public Map<MeasurementType, Float> getData(ModbusHandler.ModbusSerialMasterWrapper modbusSerialMasterWrapper) throws Exception {
        return modbusHandler.execute(modbusSerialMasterWrapper, (master) -> {
            try {
                float temperature = ((short) master.readInputRegisters(
                        0x0A, 0x1, 1)[0].getValue() & 0xFFFF) / 10.0f;
                float humidity = master.readInputRegisters(0x0A, 0x2, 1)[0].getValue() / 10.0f;

                Map<MeasurementType, Float> _data = new HashMap<>();
                _data.put(TEMPERATURE, temperature);
                _data.put(HUMIDITY, humidity);

                return _data;
            } catch (ModbusException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
