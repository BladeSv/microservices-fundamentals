package by.mitrakhovich.resourceprocessor.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();
//        List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
//        interceptors.add(new UserContextInterceptor());
//        template.setInterceptors(interceptors);

        return template;
    }
}
