package com.mindera.api.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@ToString
public class PaymentMethods {
    private String cardHolderName;
    @Size(min = 16, max = 16)
    private Long cardNumber;
    private String expireDate;
    @Size(min = 3, max = 3)
    private Long cvv;
}