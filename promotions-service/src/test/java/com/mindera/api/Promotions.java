package com.mindera.api;

import com.mindera.api.domain.Promotion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Promotions {
    PROMOTIONS_EXAMPLE(Promotion.builder()
            .id(1L)
            .build());

    private final Promotion promotion;

}
