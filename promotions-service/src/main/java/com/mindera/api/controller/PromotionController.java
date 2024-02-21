package com.mindera.api.controller;

import com.mindera.api.domain.Promotion;
import com.mindera.api.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("/promotions")
public class PromotionController extends ExceptionsController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    /*
    @GetMapping("/{uuid}")
    public ResponseEntity<Promotion> getProduct(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.getProduct(uuid));
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> getAllProductsSimplified(@PathVariable String category) {
        return ResponseEntity.ok().body(productService.getAllProductsSimplified(category));
    }

     */

    @PostMapping
    public ResponseEntity<Promotion> addPromotion(@RequestHeader("Authorization") String authorization, @RequestBody Promotion promotion) {
        return ResponseEntity.ok().body(promotionService.addPromotion(authorization, promotion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@RequestHeader("Authorization") String authorization, @RequestBody Promotion promotion, @PathVariable Long id) {
        return ResponseEntity.ok().body(promotionService.updatePromotion(authorization, promotion, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Promotion> patchPromotion(@RequestHeader("Authorization") String authorization, @RequestBody Promotion promotion, @PathVariable Long id) {
        return ResponseEntity.ok().body(promotionService.patchPromotion(authorization, promotion, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(promotionService.deletePromotion(authorization, id));
    }

    /*
    @PutMapping("/{uuid}")
    public ResponseEntity<Promotion> updateProduct(@RequestHeader("Authorization") String authorization, @RequestBody Promotion product, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.updateProduct(authorization, product, uuid));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Promotion> patchProduct(@RequestHeader("Authorization") String authorization, @RequestBody Promotion product, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(productService.patchProduct(authorization, product, uuid));
    }
     */
}
