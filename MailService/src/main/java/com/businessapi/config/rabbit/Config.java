package com.businessapi.config.rabbit;

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
    private final String queueForgetPassword = "queueForgetPassword";
    private final String keyForgetPassword = "keyForgetPassword";
    private final String queueSendMail = "queueSendMail";
    private final String keySendMail = "keySendMail";

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
    public Queue queueSendMail(){
        return new Queue(queueSendMail);
    }
    @Bean
    public Binding bindingKeySendMail(){
        return BindingBuilder.bind(queueSendMail()).to(directExchange()).with(keySendMail);
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue queueForgetPassword() {
        return new Queue(queueForgetPassword);
    }



    @Bean
    public Binding bindingForgetPassword (Queue queueForgetPassword, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueForgetPassword).to(businessDirectExchange).with(keyForgetPassword);
    }


    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
