package com.mindera.api.repository;

import com.mindera.api.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUserId(Long id);
    Optional<Cart> findByUserIdAndId(Long id, UUID uuid);

}
