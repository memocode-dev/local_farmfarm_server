package dev.memocode.local_farmfarm_server.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SensorModelInfo {
    private final String name;
    private final String description;
    private final List<MeasurementDetails> measurementDetails;
    private final SensorModelCommunicationType communicationType;

    private final int baudRate;
    private final int dataBits;
    private final String parity;
    private final int stopBits;
    private final String encoding;
    private final boolean rs485Mode;
}
