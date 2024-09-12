package com.businessapi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_QUEUE = "notificationQueue";
    public static final String NOTIFICATION_EXCHANGE = "notificationExchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notificationKey";

    @Bean
    public Queue queue() {
        return new Queue(NOTIFICATION_QUEUE, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(NOTIFICATION_ROUTING_KEY);
    }
}

