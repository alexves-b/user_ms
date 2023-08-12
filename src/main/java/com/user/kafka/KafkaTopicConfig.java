package com.user.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

	@Value(value = "${kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}
	@Bean
	public NewTopic topicAuth() {
		return new NewTopic("auth-topic", 2, (short) 2);
	}

	@Bean
	public NewTopic topicUsers() {
		return new NewTopic("users-topic", 2, (short) 2);
	}

	@Bean
	public NewTopic topicPosts() {
		return new NewTopic("posts-topic", 2, (short) 2);
	}

	@Bean
	public NewTopic topicMsg() {
		return new NewTopic("msg-topic", 2, (short) 2);
	}

	@Bean
	public NewTopic topicNotify() {
		return new NewTopic("notify-topic", 2, (short) 2);
	}

	@Bean
	public NewTopic topicFriends() {
		return new NewTopic("friends-topic", 2, (short) 2);
	}

	@Bean
	public NewTopic topicAdmin() {
		return new NewTopic("admin-topic", 2, (short) 2);
	}

}
