package com.mindera.api.repository;

import com.mindera.api.domain.CartProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartProductsRepository extends JpaRepository<CartProducts, UUID> {

    CartProducts findByCartId(UUID cartId);
}
