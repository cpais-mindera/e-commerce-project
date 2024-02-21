package com.mindera.api;

import com.mindera.api.domain.Cart;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Promotions {
    PROMOTIONS_EXAMPLE(Cart.builder()
            .id(1L)
            .build());

    private final Cart promotion;

}
