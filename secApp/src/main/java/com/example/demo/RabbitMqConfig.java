package com.example.demo;

import com.example.demo.model.CrudQueue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "crud-exchange";

    @Bean
    public DirectExchange crudExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue createQueue() {
        return new Queue(CrudQueue.CREATE.getQueueName());
    }

    @Bean
    public Queue readQueue() {
        return new Queue(CrudQueue.READ.getQueueName());
    }

    @Bean
    public Queue updateQueue() {
        return new Queue(CrudQueue.UPDATE.getQueueName());
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(CrudQueue.DELETE.getQueueName());
    }

    @Bean
    public Binding createQueueBinding() {
        return BindingBuilder.bind(createQueue()).to(crudExchange()).with(CrudQueue.CREATE.getQueueName());
    }

    @Bean
    public Binding readQueueBinding() {
        return BindingBuilder.bind(readQueue()).to(crudExchange()).with(CrudQueue.READ.getQueueName());
    }

    @Bean
    public Binding updateQueueBinding() {
        return BindingBuilder.bind(updateQueue()).to(crudExchange()).with(CrudQueue.UPDATE.getQueueName());
    }

    @Bean
    public Binding deleteQueueBinding() {
        return BindingBuilder.bind(deleteQueue()).to(crudExchange()).with(CrudQueue.DELETE.getQueueName());
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}