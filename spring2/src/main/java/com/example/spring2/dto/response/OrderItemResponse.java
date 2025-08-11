package com.example.spring2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private BigDecimal priceAtPurchaseTime;
    private Integer quantity;
}
