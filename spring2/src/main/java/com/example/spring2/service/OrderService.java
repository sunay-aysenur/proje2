package com.example.spring2.service;

import com.example.spring2.dto.request.PlaceOrderRequest;
import com.example.spring2.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(Long customerId, PlaceOrderRequest orderRequestDto);
    OrderResponse getOrderForCode(String orderCode);
    List<OrderResponse> getAllOrdersForCustomer(Long customerId);
}
