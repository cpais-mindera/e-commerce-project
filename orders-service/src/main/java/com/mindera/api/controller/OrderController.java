package com.mindera.api.controller;

import com.mindera.api.model.response.OrderResponse;
import com.mindera.api.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@ControllerAdvice
@RequestMapping("/orders")
public class OrderController extends ExceptionsController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<OrderResponse> getOrderByCartId(@RequestParam(required = true) UUID cartId) {
        return ResponseEntity.ok().body(orderService.getOrderByCartId(cartId));
    }

    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<OrderResponse> startShipping(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(orderService.startShipping(orderId));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(orderService.cancelOrder(orderId));
    }

    @PatchMapping("/{orderId}/received")
    public ResponseEntity<OrderResponse> receivedOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(orderService.receivedOrder(orderId));
    }

    @PatchMapping("/{orderId}/returned")
    public ResponseEntity<OrderResponse> returnedOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(orderService.returnedOrder(orderId));
    }
}
