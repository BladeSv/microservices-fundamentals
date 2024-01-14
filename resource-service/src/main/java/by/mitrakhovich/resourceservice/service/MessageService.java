package by.mitrakhovich.resourceservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {
    private RabbitTemplate rabbitTemplate;
    private KafkaTemplate<String, String> kafkaTemplate;
    private NewTopic topic;

    public void sentMessage(String message) {
        log.info("send to rabbitmq message-{}", message);
        kafkaTemplate.send(topic.name(), message);
    }
}
