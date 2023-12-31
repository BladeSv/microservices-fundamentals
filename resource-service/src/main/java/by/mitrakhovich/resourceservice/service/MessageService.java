package by.mitrakhovich.resourceservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {
    private RabbitTemplate rabbitTemplate;

    public void sentMessage(String message) {
        log.info("send to rabbitmq message-{}",message);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        Message messageRabbit = new Message(message.getBytes(), messageProperties);
        rabbitTemplate.send(messageRabbit);
    }
}
