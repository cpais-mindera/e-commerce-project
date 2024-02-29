package com.mindera.api.service;

import com.mindera.api.domain.Order;
import com.mindera.api.enums.Status;
import com.mindera.api.exception.*;
import com.mindera.api.message.UserRequestAndReceive;
import com.mindera.api.model.response.OrderResponse;
import com.mindera.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRequestAndReceive userRequestAndReceive;

    // TODO Add Authorization
    public OrderResponse getOrderByCartId(UUID cartId) {
        Order order = orderRepository.findByCartId(cartId)
                .orElseThrow(() -> new CartDoesNotExistsException(cartId));

        return new OrderResponse(order);
    }

    public OrderResponse startShipping(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDoesNotExistsException(orderId));

        if (!order.getOrderStatus().equals(Status.ONGOING)) {
            throw new OrderCantSwitchToThatOrderStatus(orderId, order.getOrderStatus(), Status.SHIPPED);
        }

        try {
            order.setOrderStatus(Status.SHIPPED);
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
        return new OrderResponse(order);
    }

    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDoesNotExistsException(orderId));

        if (!order.getOrderStatus().equals(Status.ONGOING)) {
            throw new OrderCantSwitchToThatOrderStatus(orderId, order.getOrderStatus(), Status.CANCEL);
        }
        try {
            order.setOrderStatus(Status.CANCEL);
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
        return new OrderResponse(order);
    }

    public OrderResponse receivedOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDoesNotExistsException(orderId));

        if (!order.getOrderStatus().equals(Status.SHIPPED)) {
            throw new OrderCantSwitchToThatOrderStatus(orderId, order.getOrderStatus(), Status.RECEIVED);
        }

        try {
            order.setOrderStatus(Status.RECEIVED);
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
        return new OrderResponse(order);
    }

    public OrderResponse returnedOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDoesNotExistsException(orderId));

        if (!order.getOrderStatus().equals(Status.RECEIVED)) {
            throw new OrderCantSwitchToThatOrderStatus(orderId, order.getOrderStatus(), Status.RETURNED);
        }
        try {
            order.setOrderStatus(Status.RETURNED);
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
        return new OrderResponse(order);
    }

}
