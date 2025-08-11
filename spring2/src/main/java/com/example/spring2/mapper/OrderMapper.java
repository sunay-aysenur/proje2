package com.example.spring2.mapper;

import com.example.spring2.dto.response.OrderItemResponse;
import com.example.spring2.dto.response.OrderResponse;
import com.example.spring2.entity.Order;
import com.example.spring2.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderCode(order.getOrderCode());
        response.setCustomerId(order.getCustomer().getId());
        response.setTotalPrice(BigDecimal.valueOf(order.getTotalPrice()));
        response.setOrderDate(order.getCreatedAt());

        List<OrderItemResponse> items = order.getOrderItems()
                .stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());

        response.setItems(items);
        return response;
    }

    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                BigDecimal.valueOf(item.getPriceAtPurchaseTime()),
                item.getQuantity()
        );
    }
}
