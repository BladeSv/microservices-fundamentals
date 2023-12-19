package by.mitrakhovich.resourceservice.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${user.aws.s3Endpoint}")
    private String s3Endpoint;
    @Value("${user.aws.awsAccessKeyId}")
    private String awsAccessKeyId;
    @Value("${user.aws.awsSecretAccessKey}")
    private String awsSecretAccessKey;
    @Value("${user.aws.awsRegion}")
    private String awsRegion;

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
}
