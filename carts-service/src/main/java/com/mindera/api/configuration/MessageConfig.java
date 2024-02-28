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

    @Value("${queues.paymentListener}")
    private String queuePaymentListenerName;

    @Value("${queues.productListener}")
    private String queueProductListenerName;

    @Value("${queues.userListener}")
    private String queueUserListenerName;

    @Value("${queues.discountListener}")
    private String queueDiscountListenerName;

    @Bean
    public Queue queueUserListener() {
        return new Queue(queueUserListenerName, true);
    }

    @Bean
    public Queue queueProductListener() {
        return new Queue(queueProductListenerName, true);
    }

    @Bean
    public Queue queuePaymentListener() {
        return new Queue(queuePaymentListenerName, true);
    }

    @Bean
    public Queue queueDiscountListener() {
        return new Queue(queueDiscountListenerName, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
