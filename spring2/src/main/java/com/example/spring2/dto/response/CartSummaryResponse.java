package com.example.spring2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryResponse {
    private Long customerId;
    private int totalItems;       // Sepetteki toplam ürün adedi (adet sayısı)
    private int distinctProducts; // Farklı ürün sayısı
    private Double totalPrice; // Sepetin toplam tutarı
}
