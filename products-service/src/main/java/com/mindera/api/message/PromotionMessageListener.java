package com.mindera.api.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.api.enums.Queues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PromotionMessageListener implements ChannelAwareMessageListener {
    private final ObjectMapper objectMapper;
    private final List<MessageInterface> messagePorts;

    @Override
    public void onMessage(Message message, Channel channel) throws IOException {
        log.info("Message received from queue {}", message.getMessageProperties().getConsumerQueue());
        PromotionMessage promotionMessage = objectMapper.readValue(message.getBody(), PromotionMessage.class);
        log.info("Body received {}", promotionMessage.toString());

        messagePorts.stream().filter(messagePort -> messagePort.canHandle(Queues.valueOf(message.getMessageProperties().getConsumerQueue())))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .processMessage(promotionMessage);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
