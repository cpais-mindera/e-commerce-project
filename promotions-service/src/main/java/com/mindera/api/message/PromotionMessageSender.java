package com.mindera.api.message;

import com.mindera.api.enums.Queues;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PromotionMessageSender {

    private RabbitTemplate rabbitTemplate;

    public void sendMessage(PromotionMessage promotionMessage) {
        rabbitTemplate.convertAndSend(Queues.PRODUCTS.name(), promotionMessage);
    }
}
