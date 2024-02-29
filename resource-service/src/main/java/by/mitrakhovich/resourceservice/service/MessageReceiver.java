package by.mitrakhovich.resourceservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Slf4j
@Service
//@Data
public class MessageReceiver {
    Logger log = LoggerFactory.getLogger(this.getClass());
    //    private NewTopic topic;
//    private RestTemplate restTemplate;
    private final SoundRecordService soundRecordService;

    public MessageReceiver(SoundRecordService soundRecordService) {
        this.soundRecordService = soundRecordService;
    }
//
//    public MessageReceiver(@Autowired @Qualifier("resource_processor_topic") NewTopic topic, RestTemplate restTemplate, SoundRecordService soundRecordService) {
//        this.topic = topic;
//        this.restTemplate = restTemplate;
//        this.soundRecordService = soundRecordService;
//    }

    @KafkaListener(topics = "#{resource_processor_topic.name()}", groupId = "resource-consumer")
    public void receiveMessageKafka(String message) {
        log.info("receive from resource-processor kafka message-{}", message);
        soundRecordService.moveToPermanentBucket(message);
    }

}
