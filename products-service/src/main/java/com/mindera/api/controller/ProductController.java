package com.mindera.api.controller;

import com.mindera.api.domain.Product;
import com.mindera.api.model.ProductDTO;
import com.mindera.api.model.ProductSimplifiedDTO;
import com.mindera.api.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    // Postman
    @GetMapping("/{uuid}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.getProduct(uuid));
    }

    // Postman
    @GetMapping
    public ResponseEntity<List<ProductSimplifiedDTO>> getAllProductsSimplified(@RequestParam(required = false) String category) {
        return ResponseEntity.ok().body(productService.getAllProductsSimplified(category));
    }

    // Postman
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product) {
        return ResponseEntity.ok().body(productService.addProduct(authorization, product));
    }

    // Postman
    @PutMapping("/{uuid}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.updateProduct(authorization, product, uuid));
    }

    // Postman
    @PatchMapping("/{uuid}")
    public ResponseEntity<ProductDTO> patchProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.patchProduct(authorization, product, uuid));
    }

    // Postman
    @PatchMapping("/{uuid}/disable")
    public ResponseEntity<ProductDTO> disableProduct(@RequestHeader("Authorization") String authorization, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.disableProduct(authorization, uuid));
    }
}
