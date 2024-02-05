package by.mitrakhovich.resourceprocessor.message;


import by.mitrakhovich.resourceprocessor.processor.Model.Song;
import by.mitrakhovich.resourceprocessor.processor.TikaSongProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;

@Component
@Slf4j
@Data
public class MessageReceiver {
    @Value("${user.resource-service}")
    private String resourceServiceUrl;
    @Value("${user.song-service}")
    private String songServiceUrl;
    private TikaSongProcessor songProcessor;
    private RestTemplate restTemplate;
    private MessageService messageService;

    public MessageReceiver(TikaSongProcessor songProcessor, RestTemplate restTemplate, MessageService messageService) {
        this.songProcessor = songProcessor;
        this.restTemplate = restTemplate;
        this.messageService = messageService;
    }

    @KafkaListener(topics = "#{resource_service_topic.name()}", groupId = "resource-consumer")
    public void receiveMessageKafka(String message) throws JsonProcessingException {
        log.info("receive from kafka resource-service topic message-{}", message);
        byte[] resourceObject = getResourceObject(message);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(resourceObject);

        Song song = songProcessor.processSound(byteArrayInputStream, message);
        sentSongMetaData(song);
        messageService.sentMessage(message);
    }

    private byte[] getResourceObject(String resourceId) {
        String url = resourceServiceUrl + resourceId;
        log.info("send request to get resource service record with url {} and id {}", url, resourceId);
        byte[] forObject = restTemplate.getForObject(url, byte[].class);
        log.info("get from resource service record with data length-{}", forObject != null ? forObject.length : "zero");
        return forObject;
    }

    @Retryable
    private void sentSongMetaData(Song song) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String songJSON = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(song);

        HttpEntity<String> request =
                new HttpEntity<>(songJSON, headers);
        log.info("send request to song service with url {} song-{}", songServiceUrl, song.toString());
        restTemplate.postForObject(songServiceUrl, request, String.class);
    }
}
