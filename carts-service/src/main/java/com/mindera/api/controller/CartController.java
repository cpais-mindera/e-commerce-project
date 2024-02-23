package com.mindera.api.controller;

import com.mindera.api.domain.Address;
import com.mindera.api.domain.PaymentMethod;
import com.mindera.api.model.CartDTO;
import com.mindera.api.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@ControllerAdvice
@RequestMapping("/carts")
public class CartController extends BaseController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body(cartService.createCart(authorization));
    }

    @PostMapping("/{cartId}/product/{productId}")
    public ResponseEntity<CartDTO> addProduct(@RequestHeader("Authorization") String authorization, @PathVariable UUID productId, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addProduct(authorization, productId, cartId));
    }

    @PostMapping("/{cartId}/discount/${discountId}")
    public ResponseEntity<CartDTO> addProduct(@RequestHeader("Authorization") String authorization, @PathVariable Long discountId, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addDiscount(authorization, discountId, cartId));
    }

    @PostMapping("/{cartId}/address")
    public ResponseEntity<CartDTO> addAddress(@RequestHeader("Authorization") String authorization, @RequestBody Address address, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addAddress(authorization, address, cartId));
    }

    @PostMapping("/{cartId}/payment")
    public ResponseEntity<CartDTO> addPayment(@RequestHeader("Authorization") String authorization, @RequestBody PaymentMethod paymentMethod, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addPayment(authorization, paymentMethod, cartId));
    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<CartDTO> removeProduct(@RequestHeader("Authorization") String authorization, @PathVariable UUID productId, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.removeProduct(authorization, productId, cartId));
    }
}
