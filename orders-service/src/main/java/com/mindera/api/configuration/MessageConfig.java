package com.mindera.api.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class MessageConfig {

    @Value("${queues.cartSender}")
    private String queueCartSenderName;

    @Value("${queues.userListener}")
    private String queueUserListenerName;

    @Bean
    public Queue queueCartSender() {
        return new Queue(queueCartSenderName, true);
    }

    @Bean
    public Queue queueUserListener() {
        return new Queue(queueUserListenerName, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
