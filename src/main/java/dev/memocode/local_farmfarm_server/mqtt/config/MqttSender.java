package dev.memocode.local_farmfarm_server.mqtt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.memocode.local_farmfarm_server.domain.exception.InternalServerException;
import dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client;
import software.amazon.awssdk.crt.mqtt5.QOS;
import software.amazon.awssdk.crt.mqtt5.packets.PublishPacket;

import java.util.UUID;

import static dev.memocode.local_farmfarm_server.domain.exception.BaseErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttSender {
    private final Mqtt5Client mqtt5Client;

    private final ObjectMapper objectMapper;

    @Value("${spring.profiles.active}")
    private String profile;

    public void send(String topic, Mqtt5Message message) {
        try {
            PublishPacket.PublishPacketBuilder publishPacketBuilder = new PublishPacket.PublishPacketBuilder();
            publishPacketBuilder.withTopic(topic);
            publishPacketBuilder.withPayload(objectMapper.writeValueAsString(message).getBytes());
            publishPacketBuilder.withQOS(QOS.AT_LEAST_ONCE);

            mqtt5Client.publish(publishPacketBuilder.build()).whenComplete((result, throwable) -> {
                if (throwable != null) {
                    log.error("Failed to publish message", throwable);
                } else {
                    log.info("Message published");
                }
            });
        } catch (Exception e) {
            log.error("Failed to sync greenhouse", e);
            throw new InternalServerException(INTERNAL_SERVER_ERROR);
        }
    }

    public void sendResponse(UUID id, Mqtt5Message message) {
        String topic = profile.equals("prod") ?
                "prod/response/%s".formatted(id.toString()) : "dev/response/%s".formatted(id.toString());
        send(topic, message);
    }
}
