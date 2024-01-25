package by.mitrakhovich.resourceservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
@PropertySource("classpath:application.properties")
public class MessageService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private NewTopic topic;


    public void sentMessage(String message) {
        log.info("send to kafka message-{}", message);
        kafkaTemplate.send(topic.name(), message);
    }
}
