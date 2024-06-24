package dev.memocode.local_farmfarm_server.mqtt;

import dev.memocode.local_farmfarm_server.domain.service.LocalHouseSectionSensorService;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseSectionSensorRequest;
import dev.memocode.local_farmfarm_server.mqtt.config.Mqtt5Body;
import dev.memocode.local_farmfarm_server.mqtt.config.MqttHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import static dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocalHouseSectionSensorController {
    private final LocalHouseSectionSensorService localHouseSectionSensorService;

    @MqttHandler(method = UPSERT, uri = "/localHouseSectionSensors")
    public void upsertLocalHouseSectionSensors(@Mqtt5Body UpsertLocalHouseSectionSensorRequest request) {
        log.info("request: {}", request);
        localHouseSectionSensorService.upsertLocalHouseSectionSensor(request);
        localHouseSectionSensorService.syncLocalHouseSectionSensor(request.getHouseSectionSensorId());
    }
}
