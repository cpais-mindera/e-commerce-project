package com.mindera.api;

import com.mindera.api.domain.Product;
import com.mindera.api.enums.Category;
import com.mindera.api.enums.Size;

public enum Products {
    PRODUCT_SMALL_WITHOUT_DISCOUNT(Product.builder()
            .id(1L)
            .description("T-shirt clean example")
            .defaultPrice(10.00)
            .discountedPrice(0.00)
            .name("T-shirt E")
            .size(Size.S)
            .stock(1)
            .category(Category.TOPS)
            .build());

    private final Product product;

    Products(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
