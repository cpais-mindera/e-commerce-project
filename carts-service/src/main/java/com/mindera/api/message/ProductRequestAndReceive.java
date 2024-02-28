package com.mindera.api.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.api.exception.ProductsInternalServerErrorException;
import com.mindera.api.message.model.ProductMessage;
import com.mindera.api.model.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductRequestAndReceive {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueProductListener;

    public ProductDTO sendRequestAndReceiveResponse(UUID productId) {
        try {
            // Create a message with the request payload
            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

            // Convert the authorization string to bytes
            ProductMessage productMessage = new ProductMessage("GET_PRODUCT_DETAILS", productId);
            ObjectMapper objectMapper = new ObjectMapper();

            Message message = new Message(objectMapper.writeValueAsBytes(productMessage), properties);

            log.info("Message sent to PRODUCT_LISTENER QUEUE: {}", productMessage.toString());

            // Send the request and wait for a response
            Message responseMessage = rabbitTemplate.sendAndReceive("PRODUCT_LISTENER", message);

            // Convert the response message to the appropriate object
            if (responseMessage != null) {
                return objectMapper.readValue(responseMessage.getBody(), ProductDTO.class);
            } else {
                // Handle no response received
                return null;
            }
        } catch (Exception e) {
            // Handle exception
            log.error("Error occurred while sending or receiving message: {}", e.getMessage());
            throw new ProductsInternalServerErrorException();
        }
    }
}
