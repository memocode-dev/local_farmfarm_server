package dev.memocode.local_farmfarm_server;

import dev.memocode.local_farmfarm_server.mqtt.config.AwsIotMqtt5Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class LocalFarmfarmServerApplication {
    private final AwsIotMqtt5Service awsIotMqtt5Service;
    private final Mqtt5Client mqtt5Client;

    public static void main(String[] args) {
        SpringApplication.run(LocalFarmfarmServerApplication.class, args);
    }

    // mqtt 시작
    @Bean
    public ApplicationRunner mqttInit() {
        return args -> awsIotMqtt5Service.start(this.mqtt5Client);
    }
}
