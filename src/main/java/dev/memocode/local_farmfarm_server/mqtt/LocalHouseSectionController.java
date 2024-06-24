package dev.memocode.local_farmfarm_server.mqtt;

import dev.memocode.local_farmfarm_server.domain.service.LocalHouseSectionService;
import dev.memocode.local_farmfarm_server.domain.service.request.UpsertLocalHouseSectionRequest;
import dev.memocode.local_farmfarm_server.mqtt.config.Mqtt5Body;
import dev.memocode.local_farmfarm_server.mqtt.config.MqttHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import static dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocalHouseSectionController {
    private final LocalHouseSectionService localHouseSectionService;

    @MqttHandler(method = UPSERT, uri = "/localHouseSections")
    public void upsertLocalHouseSection(@Mqtt5Body UpsertLocalHouseSectionRequest request) {
        log.info("request: {}", request);
        localHouseSectionService.upsertLocalHouseSection(request);
        localHouseSectionService.syncLocalHouseSection(request.getHouseSectionId());
    }
}
