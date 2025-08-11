package com.example.spring2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{

    private Integer quantity; // müşterinin kaç adet ürün aldığını gösterir.
    private Double priceAtPurchaseTime; // Ürünün, siparişin verildiği zamandaki fiyatını temsil eder.

    @ManyToOne // Bir sipariş birden fazla sipariş öğesi içerebilir.
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne // Bir ürün birden çok sipariş öğesinde yer alabilir.
    @JoinColumn(name = "product_id")
    private Product product;
}
