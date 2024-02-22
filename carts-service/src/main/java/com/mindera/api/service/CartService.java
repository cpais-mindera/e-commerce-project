package com.mindera.api.service;

import com.mindera.api.domain.Address;
import com.mindera.api.domain.Cart;
import com.mindera.api.domain.PaymentMethod;
import com.mindera.api.domain.Product;
import com.mindera.api.exception.*;
import com.mindera.api.message.PaymentMessage;
import com.mindera.api.message.PaymentMessageSender;
import com.mindera.api.model.User;
import com.mindera.api.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;
    private final PaymentMessageSender paymentMessageSender;

    public Cart createCart(String authorization) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, User.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        try {
            Cart cart = Cart.builder().userId(user.getBody().getId()).build();
            cartRepository.save(cart);
            return cart;
        } catch (DataIntegrityViolationException ex) {
            switch(ex.getCause()) {
                case ConstraintViolationException constraintEx -> {
                    String constraintName = constraintEx.getConstraintName();
                    throw new CartDuplicateException(constraintName);
                }
                case PropertyValueException propertyEx -> {
                    String nullableValue = propertyEx.getPropertyName();
                    throw new CartNotNullPropertyException(nullableValue);
                }
                default -> throw ex;
            }
        }
    }

    public Cart addProduct(String authorization, Product product, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, User.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartDoesNotExistsException(cartId));

        try {
            ResponseEntity<Product> productResponse = restTemplate.exchange("http://localhost:8082/products/" + product.getId(), HttpMethod.GET, httpEntity, Product.class);
            if (productResponse.getBody() == null) {
                throw new ProductsInternalServerErrorException();
            }
            cart.getProductList().add(product);

            double totalPrice = cart.getProductList().stream().mapToDouble(product1 -> product1.getDefaultPrice() - product1.getDiscountedPrice()).sum();
            cart.setTotalPrice(totalPrice);

            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting product inside cart: " + cart);
        }
        return cart;
    }

    public Cart addAddress(String authorization, Address address, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, User.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartDoesNotExistsException(cartId));
        try {
            cart.setAddress(address);
            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting address inside cart: " + cart);
        }
        return cart;
    }

    public Cart addPayment(String authorization, PaymentMethod paymentMethod, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, User.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }

        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartDoesNotExistsException(cartId));

        // Send Message sync through payment service
        PaymentMessage paymentMessage = new PaymentMessage();
        paymentMessage.setEventType("ADD_PAYMENT");
        paymentMessage.setCvv(paymentMethod.getCvv());
        paymentMessage.setCardNumber(paymentMethod.getCardNumber());
        paymentMessage.setAmount(cart.getTotalPrice());
        paymentMessage.setExpireDate(paymentMethod.getExpireDate());
        paymentMessage.setCardHolderName(paymentMethod.getCardHolderName());
        paymentMessageSender.send(paymentMessage);


        log.info("Payment Message sent to QUEUE " + paymentMessage.toString());

        // add Payment to Cart
        try {
            cart.setPaymentMethod(paymentMethod);
            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting address inside cart: " + cart);
        }
        return cart;
    }

    public Cart removeProduct(String authorization, UUID productId, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, User.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartDoesNotExistsException(cartId));

        try {
            List<Product> updatedProductList = cart.getProductList().stream().filter((product) -> !product.getId().equals(productId)).collect(Collectors.toList());
            cart.setProductList(updatedProductList);

            double totalPrice = cart.getProductList().stream().mapToDouble(product1 -> product1.getDefaultPrice() - product1.getDiscountedPrice()).sum();
            cart.setTotalPrice(totalPrice);

            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting product inside cart: " + cart);
        }
        return cart;
    }

    /*
    public Example getPromotion(Long id) {
        return promotionRepository.findById(id).orElseThrow(() -> new PromotionDoesNotExistsException(id));
    }

    public Example addPromotion(String authorization, Example promotion) {
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

    public Example updatePromotion(String authorization, Example promotion, Long id) {
        if (isAdminUser(authorization)) {
            try {
                getPromotion(id);
                Example updatedPromotion = Example.builder()
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

    public Example patchPromotion(String authorization, Example promotion, Long id) {
        if (isAdminUser(authorization)) {
            try {
                Example promotionDatabase = getPromotion(id);

                var promotionPatched = Example.patchPromotion(promotion, promotionDatabase);

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
            Example promotion = getPromotion(id);
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
}
