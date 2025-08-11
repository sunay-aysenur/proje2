package com.example.spring2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderCode; // örn: sipariş kodu
    private Long customerId;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;

    private List<OrderItemResponse> items;
}
