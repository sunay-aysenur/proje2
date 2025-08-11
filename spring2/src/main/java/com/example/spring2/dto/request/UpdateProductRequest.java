package com.example.spring2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @Positive
    private Double price;

    @PositiveOrZero
    private Integer stock;

    private String description;
}
