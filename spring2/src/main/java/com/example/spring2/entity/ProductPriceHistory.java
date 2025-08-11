package com.example.spring2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ProductPriceHistory extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Double price;

    private LocalDateTime changedAt;
}
