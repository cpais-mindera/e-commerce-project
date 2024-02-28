package com.mindera.api.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.api.domain.PaymentMethod;
import com.mindera.api.exception.DiscountsInternalServerErrorException;
import com.mindera.api.exception.PaymentsInternalServerErrorException;
import com.mindera.api.message.model.DiscountMessage;
import com.mindera.api.message.model.PaymentMessage;
import com.mindera.api.message.model.PaymentResponseMessage;
import com.mindera.api.model.DiscountDTO;
import com.mindera.api.model.requests.PaymentMethodRequest;
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
public class PaymentRequestAndReceive {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queuePaymentListener;

    public PaymentResponseMessage sendRequestAndReceiveResponse(PaymentMethodRequest paymentMethod, double totalPrice, UUID cartId) {
        try {
            // Create a message with the request payload
            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

            // Convert the authorization string to bytes
            PaymentMessage paymentMessage = new PaymentMessage("GET_PAYMENT_DETAILS", totalPrice, cartId);
            paymentMessage.setCvv(paymentMethod.getCvv());
            paymentMessage.setExpireDate(paymentMethod.getExpireDate());
            paymentMessage.setCardNumber(paymentMethod.getCardNumber());
            paymentMessage.setCardHolderName(paymentMethod.getCardHolderName());

            ObjectMapper objectMapper = new ObjectMapper();

            Message message = new Message(objectMapper.writeValueAsBytes(paymentMessage), properties);

            log.info("Message sent to DISCOUNT_LISTENER QUEUE: {}", paymentMessage.toString());

            // Send the request and wait for a response
            Message responseMessage = rabbitTemplate.sendAndReceive("PAYMENT_LISTENER", message);

            // Convert the response message to the appropriate object
            if (responseMessage != null) {
                return objectMapper.readValue(responseMessage.getBody(), PaymentResponseMessage.class);
            } else {
                // Handle no response received
                return null;
            }
        } catch (Exception e) {
            // Handle exception
            log.error("Error occurred while sending or receiving message: {}", e.getMessage());
            throw new PaymentsInternalServerErrorException();
        }
    }

    public DiscountDTO sendRequestAndReceiveResponse(Long discountId) {
        try {
            // Create a message with the request payload
            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

            // Convert the authorization string to bytes
            DiscountMessage discountMessage = new DiscountMessage("GET_DISCOUNT_DETAILS", discountId);
            ObjectMapper objectMapper = new ObjectMapper();

            Message message = new Message(objectMapper.writeValueAsBytes(discountMessage), properties);

            log.info("Message sent to DISCOUNT_LISTENER QUEUE: {}", discountMessage.toString());

            // Send the request and wait for a response
            Message responseMessage = rabbitTemplate.sendAndReceive("DISCOUNT_LISTENER", message);

            // Convert the response message to the appropriate object
            if (responseMessage != null) {
                return objectMapper.readValue(responseMessage.getBody(), DiscountDTO.class);
            } else {
                // Handle no response received
                return null;
            }
        } catch (Exception e) {
            // Handle exception
            log.error("Error occurred while sending or receiving message: {}", e.getMessage());
            throw new DiscountsInternalServerErrorException();
        }
    }
}
