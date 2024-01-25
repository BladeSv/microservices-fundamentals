package by.mitrakhovich.resourceservice.configuration;

import com.amazonaws.services.s3.AmazonS3;
import org.apache.kafka.clients.admin.NewTopic;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.when;


@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @Bean
    @Primary
    public AmazonS3 getMockAmazonS3() {
        return Mockito.mock(AmazonS3.class);
    }

    @Bean
    @Primary
    public NewTopic getMockNewTopic() {
        NewTopic newTopic = Mockito.mock(NewTopic.class);
        when(newTopic.name()).thenReturn("resource-topic");
        return newTopic;
    }
}
