package com.mindera.api.repository;

import com.mindera.api.domain.CartPayment;
import com.mindera.api.domain.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartPaymentsRepository extends JpaRepository<CartPayment, UUID> {

}
