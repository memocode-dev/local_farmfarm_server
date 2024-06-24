package dev.memocode.local_farmfarm_server.mqtt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import dev.memocode.local_farmfarm_server.mqtt.dto.Mqtt5Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.UriTemplate;
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client;
import software.amazon.awssdk.crt.mqtt5.Mqtt5ClientOptions;
import software.amazon.awssdk.crt.mqtt5.PublishReturn;
import software.amazon.awssdk.crt.mqtt5.packets.PublishPacket;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Mqtt5PublishEvents implements Mqtt5ClientOptions.PublishEvents {

    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;
    private final Map<String, Method> methodMap = new HashMap<>();
    private final Map<String, UriTemplate> uriTemplateMap = new HashMap<>();

    // 모든 @Controller 빈을 검색하여 @MqttHandler 어노테이션이 달린 메서드를 등록
    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        String[] controllerBeans = applicationContext.getBeanNamesForAnnotation(Controller.class);
        for (String beanName : controllerBeans) {
            Object bean = applicationContext.getBean(beanName);
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(MqttHandler.class)) {
                    MqttHandler mqttHandler = method.getAnnotation(MqttHandler.class);
                    String key = mqttHandler.uri() + ":" + mqttHandler.method();
                    methodMap.put(key, method);
                    uriTemplateMap.put(key, new UriTemplate(mqttHandler.uri()));
                }
            }
        }
    }

    @Override
    public void onMessageReceived(Mqtt5Client client, PublishReturn publishReturn) {
        PublishPacket publishPacket = publishReturn.getPublishPacket();

        try {
            Mqtt5Message mqtt5Message = objectMapper.readValue(publishPacket.getPayload(), Mqtt5Message.class);

            // 매칭되는 메서드 찾기
            Method method = null;
            Map<String, String> pathVariables = null;

            for (String key : methodMap.keySet()) {
                UriTemplate uriTemplate = uriTemplateMap.get(key);
                if (uriTemplate.matches(mqtt5Message.getUri())) {
                    method = methodMap.get(key);
                    pathVariables = uriTemplate.match(mqtt5Message.getUri());
                    break;
                }
            }

            if (method != null) {
                Object controller = applicationContext.getBean(method.getDeclaringClass());
                Object[] args = prepareMethodArguments(method, mqtt5Message, pathVariables);
                method.invoke(controller, args);
            } else {
                throw new RuntimeException("존재하지 않는 요청입니다. method: %s, uri: %s"
                        .formatted(mqtt5Message.getMethod(), mqtt5Message.getUri()));
            }
        } catch (UnrecognizedPropertyException e) {
            log.info("등록되지 않은 필드가 추가되어있습니다.", e);
        } catch (IOException e) {
            log.info("메시지를 올바르게 파싱하지 못하였습니다.", e);
        } catch (Exception e) {
            log.info("예상치 못한 에러가 발생했습니다.", e);
        }
    }

    private Object[] prepareMethodArguments(Method method, Mqtt5Message mqtt5Message, Map<String, String> pathVariables) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(Mqtt5Body.class)) {
                Class<?> parameterType = parameters[i].getType();
                try {
                    if (parameterType.equals(String.class)) {
                        args[i] = mqtt5Message.getData().toString();
                    } else {
                        args[i] = objectMapper.convertValue(mqtt5Message.getData(), parameterType);
                    }
                } catch (IllegalArgumentException e) {
                    log.warn("Failed to convert value for parameter: {}. Using default constructor.", parameters[i].getName(), e);
                    // 기본 생성자를 사용하여 객체 생성
                    try {
                        args[i] = parameterType.getDeclaredConstructor().newInstance();
                    } catch (Exception ex) {
                        log.error("Failed to create an instance for parameter: {}. Assigning null.", parameters[i].getName(), ex);
                        args[i] = null;
                    }
                }
            } else {
                String paramName = parameters[i].getName();
                if (pathVariables != null && pathVariables.containsKey(paramName)) {
                    args[i] = pathVariables.get(paramName);
                } else {
                    args[i] = null; // 필요에 따라 다른 주입 로직 추가
                }
            }
        }
        return args;
    }
}
