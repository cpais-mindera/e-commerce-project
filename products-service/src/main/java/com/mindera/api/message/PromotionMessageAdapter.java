package com.mindera.api.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.api.enums.EventType;
import com.mindera.api.enums.Queues;
import com.mindera.api.exception.ProductDoesNotExistsException;
import com.mindera.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromotionMessageAdapter implements MessageInterface {

    private ObjectMapper objectMapper;
    private ProductRepository productRepository;

    public PromotionMessageAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void processMessage(PromotionMessage promotionMessage) {
        if (promotionMessage.getEventType() == EventType.ADD_PROMOTION) {
            if (promotionMessage.getProductId() != null) {
                var product = productRepository.findById(promotionMessage.getProductId()).orElseThrow(() -> new ProductDoesNotExistsException(promotionMessage.getProductId()));
                product.setDiscountedPrice(product.getDefaultPrice() * promotionMessage.getDiscount());
            }

            if (promotionMessage.getCategory() != null) {
                var productList = productRepository.findAllByCategory(promotionMessage.getCategory());
                productList.forEach(product -> product.setDiscountedPrice(product.getDiscountedPrice() * promotionMessage.getDiscount()));
            }

            if (promotionMessage.isApplyToAllProducts()) {
                productRepository.findAll().forEach(product -> product.setDiscountedPrice(product.getDiscountedPrice() * promotionMessage.getDiscount()));
            }
        }
    }

    @Override
    public boolean canHandle(Queues queue) {
        return Queues.PROMOTIONS.equals(queue);
    }
}
