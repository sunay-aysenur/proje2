package com.example.spring2.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToCartRequest {
    @NotNull
    private Long productId;

    @Positive
    private Integer quantity;
}
