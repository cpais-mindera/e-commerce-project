package com.mindera.api.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodRequest {

    private String cardHolderName;
    private String cardNumber;
    private String expireDate;
    private Integer cvv;
}
