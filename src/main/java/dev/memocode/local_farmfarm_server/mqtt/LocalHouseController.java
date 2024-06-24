package dev.memocode.local_farmfarm_server.mqtt;

import dev.memocode.local_farmfarm_server.domain.service.LocalHouseService;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseRequest;
import dev.memocode.local_farmfarm_server.mqtt.config.Mqtt5Body;
import dev.memocode.local_farmfarm_server.mqtt.config.MqttHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import static dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocalHouseController {
    private final LocalHouseService localHouseService;

    @MqttHandler(method = UPSERT, uri = "/localHouses")
    public void upsertLocalHouse(@Mqtt5Body UpsertLocalHouseRequest request) {
        log.info("request: {}", request);
        localHouseService.upsertLocalHouse(request);
        localHouseService.syncLocalHouse(request.getHouseId());
    }
}
