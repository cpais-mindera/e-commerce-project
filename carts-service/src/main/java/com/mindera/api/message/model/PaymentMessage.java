package com.mindera.api.message.model;

import com.mindera.api.domain.PaymentMethod;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentMessage extends PaymentMethod {
    private String eventType;
    private double amount;
    private UUID cartId;
}
