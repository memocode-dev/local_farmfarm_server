package dev.memocode.local_farmfarm_server.mqtt.config;

import dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttHandler {
    String uri();
    Mqtt5Method method();
}
