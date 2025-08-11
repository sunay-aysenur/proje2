package com.example.spring2.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Product extends BaseEntity{
    private String name;
    private Double price;
    private Integer stock;
    private String description; // ürün açıklaması

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) //Bir Product nesnesinin birden fazla ProductPriceHistory kaydı olabilir.
    private List<ProductPriceHistory> priceHistoryList = new ArrayList<>();
}
