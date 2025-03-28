package com.example.bookingservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setReplyTimeout(5000); // Настройка таймаута для ожидания ответа
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        rabbitTemplate.setUseDirectReplyToContainer(false);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue checkCustomerQueue() {
        return new Queue("customer.check.email", true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("booking-exchange");
    }

    @Bean
    public Binding binding(Queue checkCustomerQueue, TopicExchange exchange) {
        return BindingBuilder.bind(checkCustomerQueue).to(exchange).with("customer.check.email");
    }
}
