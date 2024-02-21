package com.mindera.api.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    CUSTOMER("Customer"),
    ADMIN("Admin"),
    EMPLOYEE("Employee");

    private final String displayName;
}
