package com.mindera.api.message;

import com.mindera.api.domain.PaymentMethod;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentMessage extends PaymentMethod {
    private String eventType;
    private double amount;
}
