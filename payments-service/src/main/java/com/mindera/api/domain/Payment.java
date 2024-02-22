package com.mindera.api.domain;

import com.mindera.api.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "payments")
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Column
    private UUID cartId;
    @Column
    private String cardHolderName;
    @Size(min = 16, max = 16)
    @Column
    private Long cardNumber;
    @Column
    private String expireDate;
    @Size(min = 3, max = 3)
    @Column
    private Long cvv;
    @Column
    private Status paymentStatus;
}
