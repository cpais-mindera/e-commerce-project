package com.mindera.api.controller;

import com.mindera.api.domain.Address;
import com.mindera.api.domain.Cart;
import com.mindera.api.domain.PaymentMethod;
import com.mindera.api.domain.Product;
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
    public ResponseEntity<Cart> createCart(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body(cartService.createCart(authorization));
    }

    @PostMapping("/{cartId}/product")
    public ResponseEntity<Cart> addProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addProduct(authorization, product, cartId));
    }

    @PostMapping("/{cartId}/address")
    public ResponseEntity<Cart> addAddress(@RequestHeader("Authorization") String authorization, @RequestBody Address address, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addAddress(authorization, address, cartId));
    }

    @PostMapping("/{cartId}/payment")
    public ResponseEntity<Cart> addPayment(@RequestHeader("Authorization") String authorization, @RequestBody PaymentMethod paymentMethod, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addPayment(authorization, paymentMethod, cartId));
    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<Cart> removeProduct(@RequestHeader("Authorization") String authorization, @PathVariable UUID productId, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.removeProduct(authorization, productId, cartId));
    }
}
