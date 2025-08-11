package com.example.spring2.repository;

import com.example.spring2.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerId(Long customerId); //Verilen customerId değerine sahip olan sepeti getir.
    //Optional sayesinde null kontrolü daha güvenli şekilde yapılır.
}
