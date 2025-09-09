package com.example.spring2.service;

import com.example.spring2.dto.request.UpdateCartRequest;
import com.example.spring2.dto.response.CartResponse;
import com.example.spring2.dto.response.CartSummaryResponse;

public interface CartService {
    CartResponse getCart(Long customerId);
    CartResponse updateCart(Long cartId, UpdateCartRequest request);
    void emptyCart(Long customerId);
    CartResponse addProductToCart(Long customerId, Long productId, int quantity);
    CartResponse removeProductFromCart(Long customerId, Long productId);
    CartSummaryResponse getCartSummary(Long customerId);
}
