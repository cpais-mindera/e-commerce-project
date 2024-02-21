package com.mindera.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@ToString
public class Contacts {
    @Column(unique = true, nullable = false)
    private String email;
    private String phoneNumber;
}
