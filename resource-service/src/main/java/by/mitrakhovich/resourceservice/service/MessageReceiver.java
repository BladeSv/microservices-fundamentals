package by.mitrakhovich.resourceservice.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Data
public class MessageReceiver {
    private NewTopic topic;
    private RestTemplate restTemplate;
    private SoundRecordService soundRecordService;

    public MessageReceiver(@Autowired @Qualifier("resource_processor_topic") NewTopic topic, RestTemplate restTemplate, SoundRecordService soundRecordService) {
        this.topic = topic;
        this.restTemplate = restTemplate;
        this.soundRecordService = soundRecordService;
    }

    @KafkaListener(topics = "#{resource_processor_topic.name()}", groupId = "resource-consumer")
    public void receiveMessageKafka(String message) {
        log.info("receive from resource-processor kafka message-{}", message);
        soundRecordService.moveToPermanentBucket(message);
    }

}
