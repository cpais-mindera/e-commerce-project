package com.mindera.api.controller;

import com.mindera.api.domain.Promotion;
import com.mindera.api.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("/promotions")
public class PromotionController extends BaseController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

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
}
