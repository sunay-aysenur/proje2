package com.example.spring2.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long cartId) {
        super("Cart not found with ID: " + cartId);
    }
}
