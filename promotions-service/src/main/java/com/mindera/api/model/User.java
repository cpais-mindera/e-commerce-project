package com.mindera.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String vatNumber;
    @Column
    private Integer age;
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Embedded
    private Contacts contacts;
    @Embedded
    private PaymentMethods paymentMethods;
    @Embedded Address address;
    @Column
    private String password;
    @Column
    private Role role;
    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public User(String username, String vatNumber, Integer age, Gender gender,
                Contacts contacts, PaymentMethods paymentMethods, Address address,
                String password, Role role, UserStatus status) {
        this.username = username;
        this.vatNumber = vatNumber;
        this.age = age;
        this.gender = gender;
        this.contacts = contacts;
        this.paymentMethods = paymentMethods;
        this.address = address;
        this.password = password;
        this.role = role;
        this.status = status;
    }
}
