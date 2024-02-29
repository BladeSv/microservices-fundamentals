package by.mitrakhovich.resourceservice.service;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
//@NoArgsConstructor
//@Slf4j
//@Data
public class MessageService {
    Logger log = LoggerFactory.getLogger(this.getClass());


    private final KafkaTemplate<String, String> kafkaTemplate;

    //    @Qualifier("resource_service_topic")
    private final NewTopic topic;

    public MessageService(KafkaTemplate<String, String> kafkaTemplate, @Qualifier("resource_service_topic") NewTopic topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    //    public MessageService(KafkaTemplate<String, String> kafkaTemplate, NewTopic topic) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.topic = topic;
//    }

    public void sentMessage(String message) {
        log.info("send to kafka resource-service topic message-{}", message);
        kafkaTemplate.send(topic.name(), message);
    }
}
