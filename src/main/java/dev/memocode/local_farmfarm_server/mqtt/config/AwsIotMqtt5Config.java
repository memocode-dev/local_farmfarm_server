package dev.memocode.local_farmfarm_server.mqtt.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client;
import software.amazon.awssdk.crt.mqtt5.packets.ConnectPacket;
import software.amazon.awssdk.iot.AwsIotMqtt5ClientBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsIotMqtt5Config {

    @Value("${custom.aws.iot.endPoint}")
    private String AWS_IOT_ENDPOINT;

    @Value("${custom.aws.iot.certificatePath}")
    private String CERTIFICATE_PATH;

    @Value("${custom.aws.iot.privateKeyPath}")
    private String PRIVATE_KEY_PATH;

    @Value("${custom.thing.name}")
    private String THING_NAME;

    private final Mqtt5PublishEvents publishEvents;

    // Mqtt5Client
    @Bean
    public Mqtt5Client mqtt5Client(Mqtt5LifecycleEvents lifecycleEvents) {

        if (Files.exists(Paths.get(CERTIFICATE_PATH))) {
            log.info("CERTIFICATE_PATH: {}, EXISTS", CERTIFICATE_PATH);
        } else {
            log.error("CERTIFICATE NOT EXISTS");
        }

        if (Files.exists(Paths.get(PRIVATE_KEY_PATH))) {
            log.info("PRIVATE_KEY_PATH: {}, EXISTS", PRIVATE_KEY_PATH);
        } else {
            log.error("PRIVATE_KEY NOT EXISTS");
        }

        // MqttClientConnection을 만들기 위한 빌더
        AwsIotMqtt5ClientBuilder builder = AwsIotMqtt5ClientBuilder.newDirectMqttBuilderWithMtlsFromPath(
                AWS_IOT_ENDPOINT, CERTIFICATE_PATH, PRIVATE_KEY_PATH);

        // connection 환경설정
        ConnectPacket.ConnectPacketBuilder connectProperties = new ConnectPacket.ConnectPacketBuilder();
        // thing name으로 변경
        connectProperties.withClientId(THING_NAME);
        builder.withConnectProperties(connectProperties);

        // 연결 시도, 성공, 실패, 끊김, 중지가 났을 때 이 lifecycleEvent로 이벤트가 발행됨
        builder.withLifeCycleEvents(lifecycleEvents);

        builder.withPublishEvents(publishEvents);

        Mqtt5Client client = builder.build();

        builder.close();
        return client;
    }
}
