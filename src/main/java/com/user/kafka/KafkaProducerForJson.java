package com.user.kafka;

import com.user.dto.account.AccountForFriends;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerForJson {

    private final KafkaTemplate<String, AccountForFriends> accountForFriendsKafkaTemplate;

    public void sendMessageForFriends(AccountForFriends accountForFriends) {
        log.info(String.format("Message sent --> %s", accountForFriends.toString()));

        Message<AccountForFriends> message = MessageBuilder
                .withPayload(accountForFriends)
                .setHeader(KafkaHeaders.TOPIC, "friends-topic")
                .build();

        accountForFriendsKafkaTemplate.send(message);
    }
}
