package com.mindera.api;

import com.mindera.api.domain.Example;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Promotions {
    PROMOTIONS_EXAMPLE(Example.builder()
            .id(1L)
            .build());

    private final Example promotion;

}
