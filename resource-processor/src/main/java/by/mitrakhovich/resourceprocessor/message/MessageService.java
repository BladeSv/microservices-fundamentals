package by.mitrakhovich.resourceprocessor.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
public class MessageService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    @Qualifier("resource_processor_topic")
    private NewTopic topic;

    public void sentMessage(String message) {
        log.info("send to kafka resource-processor topic message-{}", message);
        kafkaTemplate.send(topic.name(), message);
    }
}
