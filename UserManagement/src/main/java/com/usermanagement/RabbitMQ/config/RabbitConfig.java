package com.usermanagement.RabbitMQ.config;

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
public class RabbitConfig {
    private final String businessDirectExchange = "businessDirectExchange";

    //Auth'dan register olup otomatik oluşturularacak user'lar için queue ve key
    private final String queueSaveUserFromAuth = "queueSaveUserFromAuth";
    private final String keySaveUserFromAuth = "keySaveUserFromAuth";


    // Bir user mail adresini update ettiğinde bunun auth'da da update edilmesi için gerekli queue ve key
    private final String queueAuthMailUpdateFromUser = "queueAuthMailUpdateFromUser";
    private final String keyAuthMailUpdateFromUser = "keyAuthMailUpdateFromUser";


    //userdan customer oluştuma
    private final String queueSaveCustomerFromUser = "queueSaveCustomerFromUser";
    private final String keySaveCustomerFromUser = "keySaveCustomerFromUser";


    //GelenAuthId ile security için Rolleri gönderme
    private final String queueRolesByAuthId = "queueRolesByAuthId";
    private final String keyRolesByAuthId = "keyRolesByAuthId";

    @Bean
    public DirectExchange businessDirectExchange() {
        return new DirectExchange(businessDirectExchange);
    }





    @Bean
    public Queue queueSaveUserFromAuth() {
        return new Queue(queueSaveUserFromAuth);
    }

    @Bean
    public Queue queueAuthMailUpdateFromUser() {
        return new Queue(queueAuthMailUpdateFromUser);
    }

    @Bean
    public Queue queueSaveCustomerFromUser() {
        return new Queue(queueSaveCustomerFromUser);
    }

    @Bean Queue queueRolesByAuthId() {
        return new Queue(queueRolesByAuthId);
    }



    @Bean
    public Binding bindingAuthMailUpdateFromUserBinding(Queue queueAuthMailUpdateFromUser, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueAuthMailUpdateFromUser).to(businessDirectExchange).with(keyAuthMailUpdateFromUser);
    }

    @Bean
    public Binding bindingUserSaveFromAuth (Queue queueSaveUserFromAuth, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueSaveUserFromAuth).to(businessDirectExchange).with(keySaveUserFromAuth);
    }

    @Bean
    public Binding bindingCustomerSaveFromUser(Queue queueSaveCustomerFromUser, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueSaveCustomerFromUser).to(businessDirectExchange).with(keySaveCustomerFromUser);
    }

    @Bean
    public Binding bindingRolesByAuthId (Queue queueRolesByAuthId, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueRolesByAuthId).to(businessDirectExchange).with(keyRolesByAuthId);
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
