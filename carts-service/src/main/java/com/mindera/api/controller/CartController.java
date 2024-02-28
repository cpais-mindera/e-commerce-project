package com.mindera.api.controller;

import com.mindera.api.domain.Address;
import com.mindera.api.domain.PaymentMethod;
import com.mindera.api.model.requests.PaymentMethodRequest;
import com.mindera.api.model.responses.CartFullResponse;
import com.mindera.api.model.responses.CartResponse;
import com.mindera.api.model.requests.DiscountRequest;
import com.mindera.api.model.requests.ProductRequest;
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

    // Postman
    @GetMapping
    public ResponseEntity<CartFullResponse> getCart(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body(cartService.getCart(authorization));
    }

    // Postman
    @PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body(cartService.createCart(authorization));
    }

    // Postman
    @PostMapping("/{cartId}/product")
    public ResponseEntity<CartResponse> addProduct(@RequestHeader("Authorization") String authorization, @RequestBody ProductRequest product, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addProduct(authorization, product, cartId));
    }

    // Postman
    @DeleteMapping("/{cartId}/product")
    public ResponseEntity<CartResponse> removeProduct(@RequestHeader("Authorization") String authorization, @RequestBody ProductRequest product, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.removeProduct(authorization, product, cartId));
    }

    // Postman
    @PatchMapping("/{cartId}/address")
    public ResponseEntity<CartResponse> updateAddress(@RequestHeader("Authorization") String authorization, @RequestBody Address address, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.updateAddress(authorization, address, cartId));
    }

    // Postman
    @PatchMapping("/{cartId}/discount")
    public ResponseEntity<CartResponse> updateDiscount(@RequestHeader("Authorization") String authorization, @RequestBody DiscountRequest discountRequest, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.updateDiscount(authorization, discountRequest, cartId));
    }

    // Postman
    @PatchMapping("/{cartId}/payment")
    public ResponseEntity<CartResponse> addPayment(@RequestHeader("Authorization") String authorization, @RequestBody PaymentMethodRequest paymentMethodRequest, @PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.addPayment(authorization, paymentMethodRequest, cartId));
    }
}
