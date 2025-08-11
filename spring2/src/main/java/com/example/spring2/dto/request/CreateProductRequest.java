package com.example.spring2.dto.request;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class CreateProductRequest { //REST API’de ürün oluşturma işlemi için kullanılır.
    @NotBlank
    private String name;

    @Positive
    private Double price;

    @PositiveOrZero
    private Integer stock;
    
    private String description;
}
