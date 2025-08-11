package com.example.spring2.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String code) {
        super("Order not found with code: " + code);
    }
}
