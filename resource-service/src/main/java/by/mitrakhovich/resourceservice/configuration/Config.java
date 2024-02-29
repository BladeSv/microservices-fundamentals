package by.mitrakhovich.resourceservice.configuration;

import by.mitrakhovich.resourceservice.model.Storage;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;


@Configuration
public class Config {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${user.aws.s3Endpoint}")
    private String s3Endpoint;
    @Value("${user.aws.awsAccessKeyId}")
    private String awsAccessKeyId;
    @Value("${user.aws.awsSecretAccessKey}")
    private String awsSecretAccessKey;
    @Value("${user.aws.awsRegion}")
    private String awsRegion;

    @Value("${application.defaultStorages}")
    private String defaultStorages;

    @Bean("defaultStorages")
    public List<Storage> getDefaultStorages() {
        JsonMapper mapper = new JsonMapper();
        List<Storage> storages;
        try {
            storages = List.of(mapper.readValue(defaultStorages, Storage[].class));
        } catch (JsonProcessingException e) {
            log.info("Default Storages configuration is empty");
            storages = Collections.emptyList();
        }

        return storages;
    }

    @Bean
    public AWSCredentials getAWSCredentials() {
        return new BasicAWSCredentials(
                awsAccessKeyId, awsSecretAccessKey
        );
    }

    @Bean
    public AmazonS3 getAmazonS3(AWSCredentials credentials) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Endpoint, awsRegion))
                .withPathStyleAccessEnabled(true)
                .build();
    }


    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(final RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .build();
    }
}
