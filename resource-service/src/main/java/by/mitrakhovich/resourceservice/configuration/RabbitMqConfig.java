package by.mitrakhovich.resourceservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class RabbitMqConfig {

    @Value("${user.messaging.queue}")
    String queueName;
    @Value("${spring.rabbitmq.template.exchange}")
    String topicExchangeName;
    @Value("${spring.rabbitmq.template.routing-key}")
    String routingName;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Bean
    DirectExchange directExchange() {

        DirectExchange directExchange = new DirectExchange(topicExchangeName);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Queue queue() {
        Queue queue = new Queue(queueName, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    Binding binding(Queue queue, DirectExchange directExchange) {
        Binding binding = BindingBuilder.bind(queue).to(directExchange).with(routingName);
        amqpAdmin.declareBinding(binding);
        return binding;
    }

//    @PostConstruct
//    @Autowired
//    public void initRabbitMq(AmqpAdmin amqpAdmin, Queue queue, DirectExchange directExchange, Binding binding) {
//        amqpAdmin.declareExchange(directExchange);
//        amqpAdmin.declareQueue(queue);
//        amqpAdmin.declareBinding(binding);
//    }


}
