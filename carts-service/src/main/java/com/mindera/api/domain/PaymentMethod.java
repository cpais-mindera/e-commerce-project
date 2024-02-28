package com.mindera.api.domain;

import com.mindera.api.enums.PaymentStatus;
import com.mindera.api.message.model.PaymentResponseMessage;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@ToString
public class PaymentMethod {

    private String cardHolderName;
    @Size(min = 16, max = 16)
    private String cardNumber;
    private String expireDate;
    @Size(min = 3, max = 3)
    private Integer cvv;
    private PaymentStatus paymentStatus;
    private UUID paymentId;

    public PaymentMethod(PaymentResponseMessage paymentResponseMessage) {
        this.cardHolderName = paymentResponseMessage.getCardHolderName();
        this.cardNumber = paymentResponseMessage.getCardNumber();
        this.expireDate = paymentResponseMessage.getExpireDate();
        this.cvv = paymentResponseMessage.getCvv();
        this.paymentStatus = paymentResponseMessage.getPaymentStatus();
        this.paymentId = paymentResponseMessage.getUuid();
    }
}