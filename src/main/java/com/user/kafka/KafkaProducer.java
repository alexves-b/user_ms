package com.user.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.dto.kafka.CommonNotifyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    @Value("${kafka.topics.notify}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(String topicName, String message) {
        kafkaTemplate.send(topicName, message);
        log.info("--|> message '{}' sent to {}", message, topicName);
    }

    public void produceKafkaMessage(CommonNotifyDto data) {
        String message;
        try {
            message = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info(" * Notification data produced and trying send {}", data);
        sendMessage("notify-topic", message);
    }
}

