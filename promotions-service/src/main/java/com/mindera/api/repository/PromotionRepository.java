package com.mindera.api.repository;

import com.mindera.api.domain.Promotion;
import com.mindera.api.enums.PromotionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findAllByPromotionStatus(PromotionStatus promotionStatus);
}
