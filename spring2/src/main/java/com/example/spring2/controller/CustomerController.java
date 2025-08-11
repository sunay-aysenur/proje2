package com.example.spring2.controller;

import com.example.spring2.dto.request.CustomerRequest;
import com.example.spring2.dto.response.CustomerResponse;
import com.example.spring2.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(@RequestBody @Valid CustomerRequest request) {
        return ResponseEntity.ok(customerService.addCustomer(request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }
}
