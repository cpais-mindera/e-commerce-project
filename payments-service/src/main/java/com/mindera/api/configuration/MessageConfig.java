package com.mindera.api.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class MessageConfig {

    @Value("${queue.payments}")
    private String queuePaymentsName;

    @Bean
    public Queue queuePayments() {
        return new Queue(queuePaymentsName, true);
    }
}
