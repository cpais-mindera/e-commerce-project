package com.mindera.api.message;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentMessage {
    private String eventType;
    private double amount;
    private String cardHolderName;
    private Long cardNumber;
    private String expireDate;
    private Long cvv;
    private UUID cartId;
}
