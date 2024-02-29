package com.mindera.api.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueCartSender;

    public void send(UUID cartId) {
        OrderMessage orderMessage = OrderMessage.builder().cartId(cartId).eventType("CREATE_CART").build();
        rabbitTemplate.convertAndSend(queueCartSender.getName(), orderMessage);
    }
}
