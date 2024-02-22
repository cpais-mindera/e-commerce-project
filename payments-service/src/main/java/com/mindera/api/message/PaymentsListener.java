package com.mindera.api.message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentsListener {

    @RabbitListener(queues = "${queue.payments}")
    public void receive(@Payload String body) {
        System.out.println(body);
    }
}
// request reply rabbitmq spring
// wire mock service to mock payments
// stripe dev environment to proceed with payments