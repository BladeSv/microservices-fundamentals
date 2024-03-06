package by.mitr.gatewayserver.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(rs ->
                                rs.path("/hello")
                                        .filters(GatewayFilterSpec::tokenRelay)
//                                .uri("lb://storage-service")
                                        .uri("http://localhost:8083/storages")
                )
                .build();
    }
}
