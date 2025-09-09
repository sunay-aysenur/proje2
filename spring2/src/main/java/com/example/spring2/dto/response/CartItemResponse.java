package com.example.spring2.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double priceAtAddition;

    public Double getTotalPrice() {
        if (priceAtAddition == null || quantity == null) {
            return 0.0;
        }
        return priceAtAddition * quantity;
    }
}
