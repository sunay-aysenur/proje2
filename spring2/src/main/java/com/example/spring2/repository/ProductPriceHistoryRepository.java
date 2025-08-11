package com.example.spring2.repository;

import com.example.spring2.entity.ProductPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory, Long> {
    List<ProductPriceHistory> findAllByProductIdOrderByChangedAtDesc(Long productId);
}
