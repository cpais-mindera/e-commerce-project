package com.mindera.api.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final Queue paymentsQueue;

    public void send(PaymentMessage message) {
        rabbitTemplate.convertAndSend(paymentsQueue.getName(), message);
    }
}
