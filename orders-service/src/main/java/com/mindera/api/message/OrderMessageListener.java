package com.mindera.api.message;

import com.mindera.api.domain.Order;
import com.mindera.api.enums.Status;
import com.mindera.api.exception.OrderDuplicateException;
import com.mindera.api.exception.OrderNotNullPropertyException;
import com.mindera.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMessageListener {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = "CART_SENDER")
    public void receive(@Payload OrderMessage orderMessage) {
        try {
            Order order = Order.builder().orderStatus(Status.ONGOING).cartId(orderMessage.getCartId()).build();
            orderRepository.save(order);
        } catch (DataIntegrityViolationException ex) {
            switch(ex.getCause()) {
                case ConstraintViolationException constraintEx -> {
                    String constraintName = constraintEx.getConstraintName();
                    throw new OrderDuplicateException(constraintName);
                }
                case PropertyValueException propertyEx -> {
                    String nullableValue = propertyEx.getPropertyName();
                    throw new OrderNotNullPropertyException(nullableValue);
                }
                default -> throw ex;
            }
        }
    }
}
