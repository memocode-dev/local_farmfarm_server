package dev.memocode.local_farmfarm_server.modbus;

import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ModbusHandler {
    public synchronized <T> T execute(ModbusSerialMasterWrapper masterWrapper,
                                      Function<ModbusSerialMaster, T> function) throws Exception {
        connect(masterWrapper);

        T data = function.apply(masterWrapper.getMaster());

        disconnect(masterWrapper);

        return data;
    }

    private void connect(ModbusSerialMasterWrapper masterWrapper) throws Exception {
        if (!masterWrapper.isConnected()) {
            masterWrapper.connect();
        }
    }

    private void disconnect(ModbusSerialMasterWrapper masterWrapper) throws Exception {
        if (masterWrapper.isConnected()) {
            masterWrapper.disconnect();
        }
    }

    public static class ModbusSerialMasterWrapper {
        private final ModbusSerialMaster master;

        private ModbusSerialMasterWrapper(ModbusSerialMaster master) {
            this.master = master;
        }

        public static ModbusSerialMasterWrapper create(String portName, int baudRate, int dataBits, String parity,
                                                       int stopBits, String encoding, boolean rs485Mode) {
            SerialParameters serialParameters = new SerialParameters();
            serialParameters.setPortName(portName);
            serialParameters.setBaudRate(baudRate);
            serialParameters.setDatabits(dataBits);
            serialParameters.setParity(parity);
            serialParameters.setStopbits(stopBits);
            serialParameters.setEncoding(encoding);
            serialParameters.setRs485Mode(rs485Mode);
            ModbusSerialMaster master = new ModbusSerialMaster(serialParameters);
            return new ModbusSerialMasterWrapper(master);
        }

        private void connect() throws Exception {
            master.connect();
        }

        private void disconnect() {
            master.disconnect();
        }

        private ModbusSerialMaster getMaster() {
            return master;
        }

        private boolean isConnected() {
            return master.isConnected();
        }
    }
}
