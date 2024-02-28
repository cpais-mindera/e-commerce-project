package com.mindera.api.message.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindera.api.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponseMessage {

    private UUID uuid;
    private PaymentStatus paymentStatus;
    private Integer cvv;
    private UUID cartId;
    private String cardNumber;
    private String expireDate;
    private String cardHolderName;

}
