package com.example.spring2.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {

      super("Customer not found by ID: " + customerId);
    }
}
