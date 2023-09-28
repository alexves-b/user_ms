package com.user.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

	@Value(value = "${spring.application.name}")
	private String groupId;

//  No need to take anything
//
//	@KafkaListener(topics = "auth-topic", groupId = "${spring.application.name}", containerFactory = "kafkaListenerContainerFactory")
//	public void listenGroupAuth(String message) {
//		log.info("--|< message '{}' received from group '{}'", message, groupId);
//	}
}
