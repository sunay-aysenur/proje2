package com.example.spring2.controller;

import com.example.spring2.dto.request.PlaceOrderRequest;
import com.example.spring2.dto.response.OrderResponse;
import com.example.spring2.service.Impl.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;

    @PostMapping("/{customerId}")
    public ResponseEntity<OrderResponse> placeOrder(@PathVariable Long customerId,
                                                    @RequestBody @Valid PlaceOrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(customerId, request));
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<OrderResponse> getOrderByCode(@PathVariable String orderCode) {
        return ResponseEntity.ok(orderService.getOrderForCode(orderCode));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersForCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getAllOrdersForCustomer(customerId));
    }
}
