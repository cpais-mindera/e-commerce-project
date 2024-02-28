package com.mindera.api.controller;

import com.mindera.api.domain.Promotion;
import com.mindera.api.model.PromotionDTO;
import com.mindera.api.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
@RequestMapping("/promotions")
public class PromotionController extends BaseController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // Postman
    @GetMapping
    public ResponseEntity<List<PromotionDTO>> addPromotion(@RequestHeader("Authorization") String authorization, @RequestParam(required = false) String promotionStatus) {
        return ResponseEntity.ok().body(promotionService.getAllPromotions(authorization, promotionStatus));
    }

    // Postman
    @PostMapping
    public ResponseEntity<PromotionDTO> addPromotion(@RequestHeader("Authorization") String authorization, @RequestBody Promotion promotion) {
        return ResponseEntity.ok().body(promotionService.addPromotion(authorization, promotion));
    }

    // Postman
    @PutMapping("/{id}")
    public ResponseEntity<PromotionDTO> updatePromotion(@RequestHeader("Authorization") String authorization, @RequestBody Promotion promotion, @PathVariable Long id) {
        return ResponseEntity.ok().body(promotionService.updatePromotion(authorization, promotion, id));
    }

    // Postman
    @PatchMapping("/{id}")
    public ResponseEntity<PromotionDTO> patchPromotion(@RequestHeader("Authorization") String authorization, @RequestBody Promotion promotion, @PathVariable Long id) {
        return ResponseEntity.ok().body(promotionService.patchPromotion(authorization, promotion, id));
    }
}
