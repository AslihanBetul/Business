package com.businessapi.config;

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
    private final String queueSaveUserFromAuth = "queueSaveUserFromAuth";
    private final String keySaveUserFromAuth = "keySaveUserFromAuth";
    private final String queueSendVerificationEmail = "queueSendVerificationEmail";
    private final String keySendVerificationEmail = "keySendVerificationEmail";
    private final String queueEmailAndPasswordFromAuth = "queueEmailAndPasswordFromAuth";
    private final String keyEmailAndPasswordFromAuth = "keyEmailAndPasswordFromAuth";
    private final String queueAuthMailUpdateFromUser = "queueAuthMailUpdateFromUser";
    private final String keyAuthMailUpdateFromUser = "keyAuthMailUpdateFromUser";
    private final String queueDeleteAuth = "queueDeleteAuth";
    private final String keyDeleteAuth = "keyDeleteAuth";

    private final String queueEmailFromCustomer = "queueEmailFromCustomer";
    private final String keyEmailFromCustomer = "keyEmailFromCustomer";
    private final String queueActivateUserFromAuth = "queueActivateUserFromAuth";
    private final String keyActivateUserFromAuth = "keyActivateUserFromAuth";



    @Bean
   public DirectExchange directExchange(){
        return new DirectExchange(businessDirectExchange);
    }

  @Bean
    public Queue queueSaveUserFromAuth(){
        return new Queue(queueSaveUserFromAuth);
    }

    @Bean
    public Queue queueSendVerificationEmail(){
        return new Queue(queueSendVerificationEmail);
    }
    @Bean
    public Queue queueDeleteAuth() {
        return new Queue(queueDeleteAuth);}
    @Bean
    public Queue queueAuthMailUpdateFromUser() {
        return new Queue(queueAuthMailUpdateFromUser);
    }
    @Bean
    public Queue queueEmailFromCustomer() {
        return new Queue(queueEmailFromCustomer);
    }
    @Bean
    public Queue queueActivateUserFromAuth() {
        return new Queue(queueActivateUserFromAuth);
    }


    @Bean
    public Queue queueEmailAndPasswordFromAuth() {
        return new Queue(queueEmailAndPasswordFromAuth);
}
    @Bean
    public Binding bindingSaveUserFromAuth(){
        return BindingBuilder.bind(queueSaveUserFromAuth()).to(directExchange()).with(keySaveUserFromAuth);
    }
    @Bean
    public Binding bindingDeleteAuth (Queue queueDeleteAuth, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueDeleteAuth).to(businessDirectExchange).with(keyDeleteAuth);
}
    @Bean
    public Binding bindingSendVerificationEmail(){
        return BindingBuilder.bind(queueSendVerificationEmail()).to(directExchange()).with(keySendVerificationEmail);
    }
    @Bean
    public Binding bindingActivateUserFromAuth (Queue queueActivateUserFromAuth, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueActivateUserFromAuth).to(businessDirectExchange).with(keyActivateUserFromAuth);
    }
    @Bean
    public Binding bindingAuthMailUpdateFromUser (Queue queueAuthMailUpdateFromUser, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueAuthMailUpdateFromUser).to(businessDirectExchange).with(keyAuthMailUpdateFromUser);
    }

    @Bean
    public Binding bindingEmailAndPasswordFromAuth (Queue queueEmailAndPasswordFromAuth, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueEmailAndPasswordFromAuth).to(businessDirectExchange).with(keyEmailAndPasswordFromAuth);
}

    @Bean
    public Binding bindingEmailFromCustomer (Queue queueEmailFromCustomer, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueEmailFromCustomer).to(businessDirectExchange).with(keyEmailFromCustomer);
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