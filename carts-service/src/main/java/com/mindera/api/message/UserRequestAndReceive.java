package com.mindera.api.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.api.exception.UserInternalServerErrorException;
import com.mindera.api.message.model.UserMessage;
import com.mindera.api.model.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRequestAndReceive {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueUserListener;

    public UserDTO sendRequestAndReceiveResponse(String authorization) {
        try {
            // Create a message with the request payload
            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

            // Convert the authorization string to bytes
            UserMessage userMessage = new UserMessage("VALIDATE_USER", authorization);
            ObjectMapper objectMapper = new ObjectMapper();

            Message message = new Message(objectMapper.writeValueAsBytes(userMessage), properties);


            log.info("Message sent to USER_LISTENER QUEUE: {}", userMessage.toString());

            // Send the request and wait for a response
            Message responseMessage = rabbitTemplate.sendAndReceive("USER_LISTENER", message);

            // Convert the response message to the appropriate object
            if (responseMessage != null) {
                // objectMapper = new ObjectMapper();
                return objectMapper.readValue(responseMessage.getBody(), UserDTO.class);
            } else {
                // Handle no response received
                return null;
            }
        } catch (Exception e) {
            // Handle exception
            log.error("Error occurred while sending or receiving message: {}", e.getMessage());
            throw new UserInternalServerErrorException();
        }
    }
}
