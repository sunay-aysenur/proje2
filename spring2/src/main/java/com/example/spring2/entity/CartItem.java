package com.example.spring2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    private Double priceAtAddition; // ürünün sepete eklendiği anda sahip olduğu fiyatı tutar.

    @Column(nullable = false)
    private Integer quantity; // müşterinin sepete ne kadar ürün eklediğini gösterir.

    @ManyToOne //Bir sepetin içinde birden fazla sepet ürünü (cart items) olabilir.
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne //Bir ürün birden fazla sepette yer alabilir.
    @JoinColumn(name = "product_id")
    private Product product;
}
