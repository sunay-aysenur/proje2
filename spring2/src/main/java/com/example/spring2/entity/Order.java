package com.example.spring2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends BaseEntity {
    private String orderCode;
    private LocalDateTime orderDate;
    private Double totalPrice = 0.0;

    @ManyToOne // Bir müşterinin birden fazla siparişi olabilir.
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // order nesnesi birden çok order item içeriyor
    private List<OrderItem> orderItems = new ArrayList<>();
}
