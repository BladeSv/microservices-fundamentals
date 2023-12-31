package by.mitrakhovich.resourceprocessor.message;


import by.mitrakhovich.resourceprocessor.processor.Model.Song;
import by.mitrakhovich.resourceprocessor.processor.SongProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;


@Component
@Slf4j
public class MessageReceiver {

    @Value("${user.resource-service}")
    private String resourceServiceUrl;
    @Value("${user.song-service}")
    private String songServiceUrl;
    private SongProcessor songProcessor;

    public MessageReceiver(SongProcessor songProcessor) {
        this.songProcessor = songProcessor;
    }

    @RabbitListener(queues = "${user.messaging.queue}")
    public void receiveMessage(String message) throws JsonProcessingException {
        log.info("receive from rabbitmq message-{}",message);
        byte[] resourceObject = getResourceObject(message);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(resourceObject);

        Song song = songProcessor.processSound(byteArrayInputStream, message);
        sentSongMetaData(song);
    }


    private byte[] getResourceObject(String resourceId) {
        log.info("send request to resource service record with id-{}",resourceId);
        RestTemplate restTemplate = new RestTemplate();
        String url = resourceServiceUrl + resourceId;
        byte[] forObject = restTemplate.getForObject(url, byte[].class);
        log.info("get from resource service record with data length-{}", forObject.length);
        return forObject;
    }

    @Retryable
    private void sentSongMetaData(Song song) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String songJSON = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(song);

        HttpEntity<String> request =
                new HttpEntity<String>(songJSON, headers);
        log.info("send request to song service with url {} song-{}", songServiceUrl, song.toString());
        restTemplate.postForObject(songServiceUrl, request, String.class);
    }
}
