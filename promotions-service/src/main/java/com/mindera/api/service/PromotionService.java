package com.mindera.api.service;

import com.mindera.api.domain.*;
import com.mindera.api.enums.EventType;
import com.mindera.api.enums.PromotionStatus;
import com.mindera.api.exception.*;
import com.mindera.api.message.PromotionMessage;
import com.mindera.api.message.PromotionMessageSender;
import com.mindera.api.model.Role;
import com.mindera.api.model.User;
import com.mindera.api.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final RestTemplate restTemplate;
    private PromotionMessageSender promotionMessageSender;

    public Promotion getPromotion(Long id) {
        return promotionRepository.findById(id).orElseThrow(() -> new PromotionDoesNotExistsException(id));
    }

    public Promotion addPromotion(String authorization, Promotion promotion) {
        if (isAdminUser(authorization)) {
            try {
                if (promotion.getPromotionStatus() == null) {
                    promotion.setPromotionStatus(PromotionStatus.ACTIVE);
                }
                promotionRepository.save(promotion);

                // Send message to RabbitMQ
                PromotionMessage promotionMessage = new PromotionMessage();
                promotionMessage.setEventType(EventType.ADD_PROMOTION);
                promotionMessage.setName(promotion.getName());
                promotionMessage.setCategory(promotion.getCategory());
                promotionMessage.setDiscount(promotion.getDiscount());
                promotionMessage.setProductId(promotion.getProductId());
                promotionMessageSender.sendMessage(promotionMessage);

                log.info(promotionMessage.toString());

                return promotion;

            } catch (DataIntegrityViolationException ex) {
                switch(ex.getCause()) {
                    case ConstraintViolationException constraintEx -> {
                        String constraintName = constraintEx.getConstraintName();
                        throw new PromotionDuplicateException(constraintName);
                    }
                    case PropertyValueException propertyEx -> {
                        String nullableValue = propertyEx.getPropertyName();
                        throw new PromotionNotNullPropertyException(nullableValue);
                    }
                    default -> throw ex;
                }
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    public Promotion updatePromotion(String authorization, Promotion promotion, Long id) {
        if (isAdminUser(authorization)) {
            try {
                getPromotion(id);
                Promotion updatedPromotion = Promotion.builder()
                        .id(id)
                        .description(promotion.getDescription())
                        .name(promotion.getName())
                        .discount(promotion.getDiscount())
                        .productId(promotion.getProductId())
                        .category(promotion.getCategory())
                        .applyToAllProducts(promotion.isApplyToAllProducts())
                        .promotionStatus(promotion.getPromotionStatus())
                        .build();

                promotionRepository.save(updatedPromotion);
                return updatedPromotion;
            } catch (DataIntegrityViolationException ex) {
                switch(ex.getCause()) {
                    case ConstraintViolationException constraintEx -> {
                        String constraintName = constraintEx.getConstraintName();
                        throw new PromotionDuplicateException(constraintName);
                    }
                    case PropertyValueException propertyEx -> {
                        String nullableValue = propertyEx.getPropertyName();
                        throw new PromotionNotNullPropertyException(nullableValue);
                    }
                    default -> throw ex;
                }
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    public Promotion patchPromotion(String authorization, Promotion promotion, Long id) {
        if (isAdminUser(authorization)) {
            try {
                Promotion promotionDatabase = getPromotion(id);

                var promotionPatched = Promotion.patchPromotion(promotion, promotionDatabase);

                promotionRepository.save(promotionPatched);

                return promotionDatabase;
            } catch (DataIntegrityViolationException ex) {
                switch(ex.getCause()) {
                    case ConstraintViolationException constraintEx -> {
                        String constraintName = constraintEx.getConstraintName();
                        throw new PromotionDuplicateException(constraintName);
                    }
                    case PropertyValueException propertyEx -> {
                        String nullableValue = propertyEx.getPropertyName();
                        throw new PromotionNotNullPropertyException(nullableValue);
                    }
                    default -> throw ex;
                }
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    public Void deletePromotion(String authorization, Long id) {
        if (isAdminUser(authorization)) {
            Promotion promotion = getPromotion(id);
            promotionRepository.delete(promotion);
        }
        throw new UserDoesNotHavePermissionsException();
    }
    /*


    public List<Promotion> getAllProductsSimplified(String category) {
        if (category != null) {
            return productRepository.findAllByCategory(Category.valueOf(category));
        }
        return productRepository.findAll();
    }




    


     */

    private boolean isAdminUser(String authorization) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<User> user = restTemplate.exchange("http://localhost:8081/users/role", HttpMethod.GET, httpEntity, User.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        return user.getBody().getRole().equals(Role.ADMIN);
    }
}
