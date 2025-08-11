package com.example.spring2.repository;

import com.example.spring2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerId(Long customerId); //Belirtilen müşteri ID’sine (customerId) sahip tüm siparişleri getir.
    Order findByOrderCode(String orderCode); //Belirtilen sipariş koduna (orderCode) sahip olan siparişi getir.
}
