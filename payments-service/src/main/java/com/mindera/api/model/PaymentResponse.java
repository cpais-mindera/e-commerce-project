package com.mindera.api.model;

import com.mindera.api.domain.Payment;
import com.mindera.api.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private UUID uuid;
    private Status paymentStatus;
    private Long cvv;
    private UUID cartId;
    private Long cardNumber;
    private String expireDate;
    private String cardHolderName;

    public PaymentResponse(Payment payment) {
        this.paymentStatus = payment.getPaymentStatus();
        this.cvv = payment.getCvv();
        this.uuid = payment.getUuid();
        this.cartId = payment.getCartId();
        this.cardNumber = payment.getCardNumber();
        this.expireDate = payment.getExpireDate();
        this.cardHolderName = payment.getCardHolderName();
    }
}
