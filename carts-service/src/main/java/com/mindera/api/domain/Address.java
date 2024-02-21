package com.mindera.api.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@ToString
public class Address {
    private String addressLine;
    private String postalCode;
    private String country;
    private String city;
}
