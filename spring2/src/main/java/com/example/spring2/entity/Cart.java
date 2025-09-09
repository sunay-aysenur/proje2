package com.example.spring2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "carts")
public class Cart extends BaseEntity{

    private Double totalPrice = 0.0;

    @OneToOne // Bir müşterinin bir sepeti var.
    @JoinColumn(name = "customer_id") // Veritabanında Cart tablosunda bir customer_id sütunu olur (foreign key).
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) // Her sepetin birden fazla CartItem'ı olabilir.
    private List<CartItem>  items = new ArrayList<>();

    public void removeCartItem(CartItem item) { //yeni eklenen metot
        items.remove(item);        // Java listeden çıkar
        item.setCart(null);        // İlişkiyi kopar
    }

}
