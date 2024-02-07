package by.mitrakhovich.resourceservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

import java.util.Map;


@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${application.kafka.resource-service.topic}")
    private String resourceServiceTopicName;

    @Value("${application.kafka.resource-processor.topic}")
    private String resourceProcessorTopicName;

    @Bean("resource_service_topic")
    public NewTopic resourceServiceTopic() {
        return TopicBuilder.name(resourceServiceTopicName).partitions(1).replicas(1).build();
    }

    @Bean("resource_processor_topic")
    public NewTopic resourceProcessorTopic() {
        return TopicBuilder.name(resourceProcessorTopicName).partitions(1).replicas(1).build();
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties, ObjectProvider<SslBundles> sslBundles) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties(sslBundles.getIfAvailable());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<String, String>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(KafkaProperties kafkaProperties, ObjectProvider<SslBundles> sslBundles) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(sslBundles.getIfAvailable());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
