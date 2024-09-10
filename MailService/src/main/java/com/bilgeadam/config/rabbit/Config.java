package com.bilgeadam.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    private final String businessDirectExchange = "businessDirectExchange";
    private final String queueSendVerificationEmail = "queueSendVerificationEmail";
    private final String keySendVerificationEmail = "keySendVerificationEmail";

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(businessDirectExchange);
    }

    @Bean
    public Queue queueSendVerificationEmail(){
        return new Queue(queueSendVerificationEmail);
    }

    @Bean
    public Binding bindingSendVerificationEmail(){
        return BindingBuilder.bind(queueSendVerificationEmail()).to(directExchange()).with(keySendVerificationEmail);
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
