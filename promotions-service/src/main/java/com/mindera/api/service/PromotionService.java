package com.mindera.api.service;

import com.mindera.api.domain.*;
import com.mindera.api.enums.Category;
import com.mindera.api.enums.PromotionStatus;
import com.mindera.api.enums.Role;
import com.mindera.api.exception.*;
import com.mindera.api.message.UserRequestAndReceive;
import com.mindera.api.model.PromotionDTO;
import com.mindera.api.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final UserRequestAndReceive userRequestAndReceive;

    @Cacheable("promotions")
    public List<PromotionDTO> getAllPromotions(String authorization, String activeStatus) {
        if (activeStatus != null) {
            List<Promotion> products = promotionRepository.findAllByPromotionStatus(PromotionStatus.valueOf(activeStatus));
            return products.stream().map(PromotionDTO::new).collect(Collectors.toList());
        }
        List<Promotion> products = promotionRepository.findAll();
        return products.stream().map(PromotionDTO::new).collect(Collectors.toList());
    }

    @CacheEvict("promotions")
    public PromotionDTO addPromotion(String authorization, Promotion promotion) {
        if (isAdminUser(authorization)) {
            try {
                if (promotion.getPromotionStatus() == null) {
                    promotion.setPromotionStatus(PromotionStatus.ACTIVE);
                }
                promotionRepository.save(promotion);

                return new PromotionDTO(promotion);

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

    @CacheEvict("promotions")
    public PromotionDTO updatePromotion(String authorization, Promotion promotion, Long id) {
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
                return new PromotionDTO(updatedPromotion);
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

    @CacheEvict("promotions")
    public PromotionDTO patchPromotion(String authorization, Promotion promotion, Long id) {
        if (isAdminUser(authorization)) {
            try {
                Promotion promotionDatabase = getPromotion(id);

                var promotionPatched = Promotion.patchPromotion(promotion, promotionDatabase);

                promotionRepository.save(promotionPatched);

                return new PromotionDTO(promotionPatched);
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

    private Promotion getPromotion(Long id) {
        return promotionRepository.findById(id).orElseThrow(() -> new PromotionDoesNotExistsException(id));
    }

    private boolean isAdminUser(String authorization) {
        try {
            var test = userRequestAndReceive.sendRequestAndReceiveResponse(authorization);
            return test.getRole().equals(Role.ADMIN);
        } catch (Exception ex) {
            throw new UserInternalServerErrorException();
        }
    }
}
