package com.user.kafka;

import com.user.dto.JsonMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    @Value("${kafka.topics.auth}")
    private String topicName;

    private int messageNumber = 0;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, JsonMessage> jsonKafkaTemplate;

    /**
     * Use with Kafka 3.x.x
     *
     */

//    public void sendMessage(String message) {
//        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
//        future.whenComplete((result, ex) -> {
//            if (ex == null) {
//                System.out.println("Sent message=[" + message +
//                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
//            } else {
//                System.out.println("Unable to send message=[" +
//                        message + "] due to : " + ex.getMessage());
//            }
//        });
//    }

    /**
     * Use with Kafka 2.x.x
     * Because the futures returned by this class are now CompletableFuture s instead of ListenableFuture
     * May add guava for ListenableFuture
     */

//    public ListenableFuture<SendResult<String, String>> sendMessage(String message) {
//        log.info("Sending {}", message);
//	    return kafkaTemplate.send(topicName, message);
//    }

    public void sendMessage(String message) {
        kafkaTemplate.send(topicName, message);
        log.info("▶ message '{}' sent", message);
    }

    public void sendJsonMessage(@NotNull JsonMessage jsonMessage) {
        jsonMessage.setTimestamp(new Date());
        jsonMessage.setNumber(new Random().nextInt(564758473));
        jsonKafkaTemplate.send(topicName, jsonMessage);
        log.info("▶ message '{}' sent", jsonMessage.toString());
    }

}

