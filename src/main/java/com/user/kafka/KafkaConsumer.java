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

	private final String KEYWORD_FOR_CONSUMER = "WORLD";

	@KafkaListener(topics = "auth-topic", groupId = "${spring.application.name}", containerFactory = "kafkaListenerContainerFactory")
	public void listenGroupAuth(String message) {
		log.info("◀ message '{}' received from group '{}'", message, groupId);
	}
//
//	@KafkaListener(topics = "${kafka.topics.auth}", groupId = "${spring.application.name}", containerFactory = "filteredKafkaListenerContainerFactory")
//	public void listenFilteredMessage(String message) {
//		log.info("◀ message '{}' received from group '{}' filtered by KEYWORD '{}'", message, groupId, KEYWORD_FOR_CONSUMER);
//	}

//	@KafkaListener(topics = "auth2users", groupId = "${spring.application.name}", containerFactory = "jsonKafkaListenerContainerFactory")
//	public void jsonListener(@NotNull JsonMessage jsonMessage) {
//		log.info("◀ JSON message '{}' received from group '{}'", jsonMessage.toString(), groupId);
//	}
}
