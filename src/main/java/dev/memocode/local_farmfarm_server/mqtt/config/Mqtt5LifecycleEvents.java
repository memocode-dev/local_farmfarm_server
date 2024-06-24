package dev.memocode.local_farmfarm_server.mqtt.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.mqtt5.*;
import software.amazon.awssdk.crt.mqtt5.packets.DisconnectPacket;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class Mqtt5LifecycleEvents implements Mqtt5ClientOptions.LifecycleEvents {

    private final AwsIotMqtt5Service awsIotMqtt5Service;

    // mqtt5의 connection 여부
    private volatile boolean isConnected = false;

    // 연결 시도 이벤트
    @Override
    public void onAttemptingConnect(Mqtt5Client client, OnAttemptingConnectReturn onAttemptingConnectReturn) {
        log.info("Mqtt5 Client: 연결 시도 중...");
        this.isConnected = false;
    }

    // 연결 성공 이벤트
    @Override
    public void onConnectionSuccess(Mqtt5Client client, OnConnectionSuccessReturn onConnectionSuccessReturn) {
        log.info("Mqtt5 Client: 연결 성공!!, client ID: {}",
                onConnectionSuccessReturn.getNegotiatedSettings().getAssignedClientID());
        this.isConnected = true;
    }

    // 연결 실패 이벤트
    @Override
    public void onConnectionFailure(Mqtt5Client client, OnConnectionFailureReturn onConnectionFailureReturn) {
        String errorString = CRT.awsErrorString(onConnectionFailureReturn.getErrorCode());
        log.error("Mqtt5 Client: 연결 실패, error: {}", errorString);
        this.isConnected = false;
    }

    // 연결 끊김 이벤트
    @Override
    public void onDisconnection(Mqtt5Client client, OnDisconnectionReturn onDisconnectionReturn) {
        log.error("Mqtt5 Client: 연결 끊김!");
        DisconnectPacket disconnectPacket = onDisconnectionReturn.getDisconnectPacket();
        if (disconnectPacket != null) {
            log.error("error code : {}", disconnectPacket.getReasonCode());
            log.error("reason code : {}", disconnectPacket.getReasonString());
        }
        this.isConnected = false;

        // 재연결 로직 (예: 5초 후 재연결 시도)
        try {
            Thread.sleep(5000); // 5초 대기
            awsIotMqtt5Service.start(client);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("재연결 시도 중 인터럽트 발생", e);
        }
    }

    // 연결 중지 이벤트
    @Override
    public void onStopped(Mqtt5Client client, OnStoppedReturn onStoppedReturn) {
        log.info("Mqtt5 Client: 연결 중지!");
        this.isConnected = false;
    }
}
