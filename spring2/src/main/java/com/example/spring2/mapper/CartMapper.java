package com.example.spring2.mapper;

import com.example.spring2.dto.response.CartItemResponse;
import com.example.spring2.dto.response.CartResponse;
import com.example.spring2.entity.Cart;
import com.example.spring2.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartResponse toCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setCustomerId(cart.getCustomer().getId());
        response.setTotalPrice(cart.getTotalPrice());

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());

        response.setItems(items);
        return response;
    }

    public CartItemResponse toCartItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPriceAtAddition()
        );
    }
}
