package com.mindera.api.service;

import com.mindera.api.domain.Address;
import com.mindera.api.domain.Cart;
import com.mindera.api.domain.CartProduct;
import com.mindera.api.domain.PaymentMethod;
import com.mindera.api.exception.*;
import com.mindera.api.message.*;
import com.mindera.api.message.model.PaymentResponseMessage;
import com.mindera.api.model.*;
import com.mindera.api.model.requests.DiscountRequest;
import com.mindera.api.model.requests.PaymentMethodRequest;
import com.mindera.api.model.requests.ProductRequest;
import com.mindera.api.model.responses.CartFullResponse;
import com.mindera.api.model.responses.CartResponse;
import com.mindera.api.repository.CartProductsRepository;
import com.mindera.api.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartProductsRepository cartProductsRepository;
    private final PaymentRequestAndReceive paymentRequestAndReceive;
    private final UserRequestAndReceive userRequestAndReceive;
    private final ProductRequestAndReceive productRequestAndReceive;
    private final DiscountRequestAndReceive discountRequestAndReceive;

    public CartFullResponse getCart(String authorization) {
        UserDTO user = getUser(authorization);

        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new CartDoesNotExistsException(user.getId()));
        List<ProductDTO> productList = cart.getCartProducts().stream()
                .map(product1 -> getProduct(product1.getProductId()))
                .toList();

        DiscountDTO discount = getDiscount(cart.getDiscountId());

        return new CartFullResponse(cart, productList, user, discount);
    }

    public CartResponse createCart(String authorization) {
        var user = getUser(authorization);
        if (userAlreadyHaveCart(user)) {
            throw new CartAlreadyExistsForUserException(user.getId());
        }
        try {
            Cart cart = Cart.builder()
                    .userId(user.getId())
                    .createdAt(LocalDateTime.now())
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            cartRepository.save(cart);
            return new CartResponse(cart);
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

    public CartResponse addProduct(String authorization, ProductRequest product, UUID cartId) {
        UserDTO userResponse = getUser(authorization);
        Cart cart = cartRepository.findByUserIdAndId(userResponse.getId(), cartId)
                .orElseThrow(() -> new CartDoesNotExistsException(userResponse.getId()));


        Optional<CartProduct> optionalCartProduct = cartProductsRepository.findByCartIdAndProductId(cartId, product.getProductId());

        // Add the CartItem to the Cart's cartItems list
        if (optionalCartProduct.isEmpty()) {
            // Create a new CartItem
            CartProduct cartProduct = CartProduct.builder()
                    .cart(cart)
                    .productId(product.getProductId())
                    .quantity(product.getQuantity())
                    .build();
            cart.getCartProducts().add(cartProduct);
        } else {
            CartProduct cartProduct = optionalCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + product.getQuantity());
            cart.getCartProducts().add(cartProduct);
        }

        try {
            cartRepository.save(cart);
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
        return  new CartResponse(cart);
    }

    public CartResponse removeProduct(String authorization, ProductRequest product, UUID cartId) {
        var userResponse = getUser(authorization);
        Cart cart = cartRepository.findByUserIdAndId(userResponse.getId(), cartId)
                .orElseThrow(() -> new CartDoesNotExistsException(userResponse.getId()));

        CartProduct cartProduct = cartProductsRepository.findByCartIdAndProductId(cartId, product.getProductId())
                .orElseThrow(() -> new ProductDoesNotExistsInCartException(product.getProductId(), cartId));

        cartProduct.setQuantity(cartProduct.getQuantity() - product.getQuantity());

        try {
            if (cartProduct.getQuantity() == 0) {
                cartProductsRepository.delete(cartProduct);
            } else {
                cartProductsRepository.save(cartProduct);
            }
            // cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting product inside cart: " + cart);
        }
        return new CartResponse(cart);
    }

    public CartResponse updateAddress(String authorization, Address address, UUID cartId) {
        var userResponse = getUser(authorization);
        Cart cart = cartRepository.findByUserIdAndId(userResponse.getId(), cartId)
                .orElseThrow(() -> new CartDoesNotExistsException(userResponse.getId()));

        try {
            cart.setAddress(address);
            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting address inside cart: " + cart);
        }
        return new CartResponse(cart);
    }

    public CartResponse updateDiscount(String authorization, DiscountRequest discount, UUID cartId) {
        var userResponse = getUser(authorization);
        Cart cart = cartRepository.findByUserIdAndId(userResponse.getId(), cartId)
                .orElseThrow(() -> new CartDoesNotExistsException(userResponse.getId()));

        try {
            cart.setDiscountId(discount.getDiscountId());
            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting discount inside cart: " + cart);
        }
        return new CartResponse(cart);
    }

    public CartResponse addPayment(String authorization, PaymentMethodRequest paymentMethod, UUID cartId) {
        var userResponse = getUser(authorization);
        Cart cart = cartRepository.findByUserIdAndId(userResponse.getId(), cartId)
                .orElseThrow(() -> new CartDoesNotExistsException(userResponse.getId()));

        double totalPrice;
        if (!cart.getCartProducts().isEmpty() && cart.getAddress() != null) {
            if (cart.getDiscountId() != null ) {
                totalPrice = cart.getCartProducts().stream()
                        .mapToDouble(product -> {
                            double defaultPrice = getProduct(product.getProductId()).getDefaultPrice();
                            DiscountDTO discountDTO = getDiscount(cart.getDiscountId());
                            double discount = discountDTO.getDiscount();
                            return defaultPrice * (1 - discount);
                        })
                        .sum();
            } else {
                totalPrice = cart.getCartProducts().stream()
                        .mapToDouble(product -> getProduct(product.getProductId()).getDefaultPrice()).sum();
            }
        } else {
            throw new NeedToHaveProductsOrAddressInYourCartException();
        }

        PaymentResponseMessage paymentResponseMessage = paymentRequestAndReceive.sendRequestAndReceiveResponse(paymentMethod, totalPrice, cart.getId());

        try {
            PaymentMethod paymentMethod1 = new PaymentMethod(paymentResponseMessage);
            cart.setPaymentMethod(paymentMethod1);
            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting address inside cart: " + cart);
        }
        return new CartResponse(cart);
    }

    private boolean userAlreadyHaveCart(UserDTO userDTO) {
        return cartRepository.findByUserId(userDTO.getId()).isPresent();
    }

    private UserDTO getUser(String authorization) {
        try {
            return userRequestAndReceive.sendRequestAndReceiveResponse(authorization);
        } catch (Exception ex) {
            throw new UserInternalServerErrorException();
        }
    }

    private ProductDTO getProduct(UUID productId) {
        try {
            return productRequestAndReceive.sendRequestAndReceiveResponse(productId);
        } catch (Exception ex) {
            throw new ProductsInternalServerErrorException();
        }
    }

    private DiscountDTO getDiscount(Long discountId) {
        try {
            return discountRequestAndReceive.sendRequestAndReceiveResponse(discountId);
        } catch (Exception ex) {
            throw new DiscountsInternalServerErrorException();
        }
    }


}
