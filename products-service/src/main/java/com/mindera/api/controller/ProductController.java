package com.mindera.api.controller;

import com.mindera.api.domain.Product;
import com.mindera.api.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@ControllerAdvice
@RequestMapping("/products")
public class ProductController extends ExceptionsController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.getProduct(uuid));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProductsSimplified(@PathVariable String category) {
        return ResponseEntity.ok().body(productService.getAllProductsSimplified(category));
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product) {
        return ResponseEntity.ok().body(productService.addProduct(authorization, product));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Product> updateProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.updateProduct(authorization, product, uuid));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Product> patchProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.patchProduct(authorization, product, uuid));
    }

    // TODO Disable
}
