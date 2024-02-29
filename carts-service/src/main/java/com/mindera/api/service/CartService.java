package com.mindera.api.service;

import com.mindera.api.domain.*;
import com.mindera.api.enums.CartStatus;
import com.mindera.api.enums.PaymentStatus;
import com.mindera.api.exception.*;
import com.mindera.api.message.*;
import com.mindera.api.message.model.PaymentResponseMessage;
import com.mindera.api.model.*;
import com.mindera.api.model.requests.DiscountRequest;
import com.mindera.api.model.requests.PaymentMethodRequest;
import com.mindera.api.model.requests.ProductRequest;
import com.mindera.api.model.responses.CartFullResponse;
import com.mindera.api.model.responses.CartResponse;
import com.mindera.api.repository.CartPaymentsRepository;
import com.mindera.api.repository.CartProductsRepository;
import com.mindera.api.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartProductsRepository cartProductsRepository;
    private final CartPaymentsRepository cartPaymentsRepository;
    private final PaymentRequestAndReceive paymentRequestAndReceive;
    private final UserRequestAndReceive userRequestAndReceive;
    private final ProductRequestAndReceive productRequestAndReceive;
    private final DiscountRequestAndReceive discountRequestAndReceive;

    public CartFullResponse getCart(String authorization, UUID cartId) {
        UserDTO user = getUser(authorization);

        Cart cart = cartRepository.findByUserIdAndId(user.getId(), cartId).orElseThrow(() -> new CartDoesNotExistsException(user.getId()));
        List<ProductDTO> productList = cart.getCartProducts().stream()
                .map(product1 ->
                        getProduct(product1.getProductId()))
                .toList();

        DiscountDTO discount = getDiscount(cart.getDiscountId());

        return new CartFullResponse(cart, productList, user, discount);
    }

    public List<CartFullResponse> getAllCartsByUser(String authorization) {
        UserDTO user = getUser(authorization);

        List<Cart> cartList = cartRepository.findAllByUserId(user.getId());

        List<CartFullResponse> cartFullResponseList = new ArrayList<>();
        for (Cart cart: cartList) {
            List<ProductDTO> productList = cart.getCartProducts().stream()
                    .map(product1 -> {
                        ProductDTO getProductDetails = getProduct(product1.getProductId());
                        getProductDetails.setId(product1.getProductId());
                        getProductDetails.setQuantity(product1.getQuantity());
                        return getProductDetails;
                    })
                    .toList();

            DiscountDTO discount = getDiscount(cart.getDiscountId());
            CartFullResponse cartFullResponse = new CartFullResponse(cart, productList, user, discount);
            cartFullResponseList.add(cartFullResponse);
        }

        return cartFullResponseList;
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
            cart.setCartStatus(CartStatus.IN_PROGRESS);
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

        if (cart.getCartStatus().equals(CartStatus.CONVERTED_TO_ORDER)) throw new CartAlreadyConvertedToOrderException();


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

        // TODO
        //  If payment is paid already we can't add new payment
        //  Else we need to pay again

        PaymentResponseMessage paymentResponseMessage = paymentRequestAndReceive.sendRequestAndReceiveResponse(paymentMethod, totalPrice, cart.getId());

        try {
            PaymentMethod paymentMethod1 = new PaymentMethod(paymentResponseMessage);
            CartPayment cartPayment = CartPayment.builder()
                    .cart(cart)
                    .paymentMethod(paymentMethod1)
                    .amount(totalPrice)
                    .build();


            cartPaymentsRepository.save(cartPayment);

            if (paymentMethod1.getPaymentStatus().equals(PaymentStatus.PAID)) {
                cart.setCartStatus(CartStatus.CONVERTED_TO_ORDER);
            }

            cartRepository.save(cart);
        } catch (Exception ex) {
            log.error("Error setting payment inside cart: " + cart);
        }
        return new CartResponse(cart);
    }


    private boolean userAlreadyHaveCart(UserDTO userDTO) {
        Optional<Cart> cartByUser = cartRepository.findByUserId(userDTO.getId());

        if (cartByUser.isEmpty()) {
            return false;
        }

        CartStatus cartStatus = cartByUser.get().getCartStatus();
        return !(cartStatus == CartStatus.CANCELLED || cartStatus == CartStatus.CONVERTED_TO_ORDER);
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
