package com.mindera.api.message;

import com.mindera.api.domain.Promotion;
import com.mindera.api.exception.PromotionDoesNotExistsException;
import com.mindera.api.model.PromotionDTO;
import com.mindera.api.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromotionMessageReceiver {

    private final PromotionRepository promotionRepository;

    @RabbitListener(queues = "DISCOUNT_LISTENER")
    public PromotionDTO receiveRequest(@Payload PromotionMessage message) {
        Long discountId = message.getDiscountId();

        Promotion promotion = promotionRepository.findById(discountId).orElseThrow(() -> new PromotionDoesNotExistsException(discountId));
        log.info("Promotion Message reply: ", promotion.toString());
        return new PromotionDTO(promotion);
    }
}
