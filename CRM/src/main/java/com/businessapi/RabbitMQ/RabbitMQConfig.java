package com.businessapi.RabbitMQ;


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
public class RabbitMQConfig {
    private final String businessDirectExchange = "businessDirectExchange";


    //userdan customer oluştuma
    private final String queueSaveCustomerFromUser = "queueSaveCustomerFromUser";
    private final String keySaveCustomerFromUser = "keySaveCustomerFromUser";

    //customer user'dan bilgileri istemesi icin kuyruk
    private final String queueRequestCustomerFromUser = "queueRequestCustomerFromUser";
    private final String keyRequestCustomerFromUser = "keyRequestCustomerFromUser";

    //customer auth'dan email bilgilerini istedigi kuyruk
    private final String queueRequestCustomerFromAuth = "queueRequestCustomerFromAuth";
    private final String keyRequestCustomerFromAuth = "keyRequestCustomerFromAuth";

    // auth'dan user'a gelen email bilgisi kuyrugu
    private final String queueEmailFromAuth = "queueEmailFromAuth";
    private final String keyEmailFromAuth = "keyEmailFromAuth";

    // stock service'e giden kuyruk
    private final String queueResponseStock = "queueRequestStock";
    private final String keyResponseStock = "keyRequestStock";


    @Bean
    public DirectExchange businessDirectExchange() {
        return new DirectExchange(businessDirectExchange);
    }


    @Bean
    public Queue queueSaveCustomerFromUser() {
        return new Queue(queueSaveCustomerFromUser);
    }
    @Bean
    public Queue queueRequestCustomerFromUser() {
        return new Queue(queueRequestCustomerFromUser);
    }
    @Bean
    public Queue queueRequestCustomerFromAuth() {
        return new Queue(queueRequestCustomerFromAuth);
    }
    @Bean
    public Queue queueEmailFromAuth() {
        return new Queue(queueEmailFromAuth);
    }
    @Bean
    public Queue queueResponseStock() {
        return new Queue(queueResponseStock);
    }



    @Bean
    public Binding bindingCustomerSaveFromUser(Queue queueSaveCustomerFromUser, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueSaveCustomerFromUser).to(businessDirectExchange).with(keySaveCustomerFromUser);
    }
    @Bean
    public Binding bindingCustomerRequestFromUser(Queue queueRequestCustomerFromUser, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueRequestCustomerFromUser).to(businessDirectExchange).with(keyRequestCustomerFromUser);
    }
    @Bean
    public Binding bindingCustomerRequestFromAuth(Queue queueRequestCustomerFromAuth, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueRequestCustomerFromAuth).to(businessDirectExchange).with(keyRequestCustomerFromAuth);
    }
    @Bean
    public Binding bindingEmailFromAuth(Queue queueEmailFromAuth, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueEmailFromAuth).to(businessDirectExchange).with(keyEmailFromAuth);
    }
    @Bean
    public Binding bindingResponseStock(Queue queueResponseStock, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueResponseStock).to(businessDirectExchange).with(keyResponseStock);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }


}
