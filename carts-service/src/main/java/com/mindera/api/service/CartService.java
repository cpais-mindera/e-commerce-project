package com.mindera.api.service;

import com.mindera.api.domain.Address;
import com.mindera.api.domain.Cart;
import com.mindera.api.domain.CartProducts;
import com.mindera.api.domain.PaymentMethod;
import com.mindera.api.domain.Product;
import com.mindera.api.exception.*;
import com.mindera.api.message.PaymentMessage;
import com.mindera.api.message.PaymentMessageSender;
import com.mindera.api.model.CartDTO;
import com.mindera.api.model.ProductDTO;
import com.mindera.api.model.UserDTO;
import com.mindera.api.repository.CartProductsRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartProductsRepository cartProductsRepository;
    private final RestTemplate restTemplate;
    private final PaymentMessageSender paymentMessageSender;

    public CartDTO createCart(String authorization) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<UserDTO> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, UserDTO.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        try {
            Cart cart = Cart.builder().userId(user.getBody().getId()).build();
            cartRepository.save(cart);
            return new CartDTO(cart);
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

    public CartDTO addProduct(String authorization, UUID productId, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<UserDTO> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, UserDTO.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartDoesNotExistsException(cartId));

        try {
            ResponseEntity<ProductDTO> productResponse = restTemplate.exchange("http://localhost:8082/products/" + productId, HttpMethod.GET, httpEntity, ProductDTO.class);
            if (productResponse.getBody() == null) {
                throw new ProductsInternalServerErrorException();
            }

            if (cart.getCartProducts().isEmpty()) {
                List<UUID> productListIds = new ArrayList<>();
                productListIds.add(productId);
                cartProductsRepository.save(CartProducts.builder().cartId(cartId).productListIds(productListIds).build());
            } else {
                CartProducts cartProducts = cartProductsRepository.findByCartId(cartId);
                cartProducts.getProductListIds().add(productId);
                cartProductsRepository.save(cartProducts);
            }

            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting product inside cart: " + cart);
        }
        return new CartDTO(cart);
    }

    public CartDTO addAddress(String authorization, Address address, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<UserDTO> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, UserDTO.class);
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
        return new CartDTO(cart);
    }

    public CartDTO addPayment(String authorization, PaymentMethod paymentMethod, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<UserDTO> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, UserDTO.class);
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
        return new CartDTO(cart);
    }

    public CartDTO removeProduct(String authorization, UUID productId, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<UserDTO> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, UserDTO.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartDoesNotExistsException(cartId));

        try {
            /* TODO update Price
            List<Product> updatedProductList = cart.getProductList().stream().filter((product) -> !product.getId().equals(productId)).collect(Collectors.toList());
            cart.setProductList(updatedProductList);

            double totalPrice = cart.getProductList().stream().mapToDouble(product1 -> product1.getDefaultPrice() - product1.getDiscountedPrice()).sum();
            cart.setTotalPrice(totalPrice);
             */

            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting product inside cart: " + cart);
        }
        return new CartDTO(cart);
    }

    public CartDTO addDiscount(String authorization, Long discountId, UUID cartId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<UserDTO> user = restTemplate.exchange("http://localhost:8081/users/login", HttpMethod.GET, httpEntity, UserDTO.class);
        if (user.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartDoesNotExistsException(cartId));
        /* TODO Add discount
        try {
            ResponseEntity<ProductDTO> productResponse = restTemplate.exchange("http://localhost:8082/products/" + productId, HttpMethod.GET, httpEntity, ProductDTO.class);
            if (productResponse.getBody() == null) {
                throw new ProductsInternalServerErrorException();
            }

            if (cart.getCartProducts().isEmpty()) {
                List<UUID> productListIds = new ArrayList<>();
                productListIds.add(productId);
                cartProductsRepository.save(CartProducts.builder().cartId(cartId).productListIds(productListIds).build());
            } else {
                CartProducts cartProducts = cartProductsRepository.findByCartId(cartId);
                cartProducts.getProductListIds().add(productId);
                cartProductsRepository.save(cartProducts);
            }

            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting product inside cart: " + cart);
        }
        return new CartDTO(cart);
        */
        return new CartDTO(cart);
    }


}
