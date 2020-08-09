package ru.sokol.smartoffice.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("rabbitmq.device.topic.name")
    String topicName;

    @Bean
    public TopicExchange topic() {
        return new TopicExchange(topicName);
    }
}
