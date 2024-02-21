package com.mindera.api.model;

import com.mindera.api.domain.Address;
import com.mindera.api.enums.Gender;
import com.mindera.api.enums.Role;
import com.mindera.api.enums.UserStatus;
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
    @Embedded
    Address address;
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

    public static User patchUser(User user, User userDatabase) {
        if (user.getPassword() != null) {
            userDatabase.setPassword(user.getPassword());
        }
        if (user.getVatNumber() != null) {
            userDatabase.setVatNumber(user.getVatNumber());
        }
        if (user.getAge() != null) {
            userDatabase.setAge(user.getAge());
        }
        if (user.getGender() != null) {
            userDatabase.setGender(user.getGender());
        }
        if (user.getContacts() != null) {
            Contacts contacts = user.getContacts();
            if (contacts.getEmail() != null) {
                userDatabase.getContacts().setEmail(contacts.getEmail());
            }
            if (contacts.getPhoneNumber() != null) {
                userDatabase.getContacts().setPhoneNumber(contacts.getPhoneNumber());
            }
        }
        if (user.getPaymentMethods() != null) {
            PaymentMethods paymentMethods = user.getPaymentMethods();

            if (paymentMethods.getCardHolderName() != null) {
                userDatabase.getPaymentMethods().setCardHolderName(paymentMethods.getCardHolderName());
            }
            if (paymentMethods.getCardNumber() != null) {
                userDatabase.getPaymentMethods().setCardNumber(paymentMethods.getCardNumber());
            }

            if (paymentMethods.getExpireDate() != null) {
                userDatabase.getPaymentMethods().setExpireDate(paymentMethods.getExpireDate());
            }

            if (paymentMethods.getCvv() != null) {
                userDatabase.getPaymentMethods().setCvv(paymentMethods.getCvv());
            }
        }
        if (user.getAddress() != null) {
            Address address = user.getAddress();
            if (address.getAddressLine() != null) {
                userDatabase.getAddress().setAddressLine(address.getAddressLine());
            }
            if (address.getCity() != null) {
                userDatabase.getAddress().setCity(address.getCity());
            }
            if (address.getCountry() != null) {
                userDatabase.getAddress().setCountry(address.getCountry());
            }
            if (address.getPostalCode() != null) {
                userDatabase.getAddress().setPostalCode(address.getPostalCode());
            }
        }
        if (user.getRole() != null) {
            userDatabase.setRole(user.getRole());
        }
        if (user.getStatus() != null) {
            userDatabase.setStatus(user.getStatus());
        }
        return userDatabase;
    }
}
