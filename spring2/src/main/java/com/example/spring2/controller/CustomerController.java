package com.example.spring2.controller;

import com.example.spring2.dto.request.CustomerRequest;
import com.example.spring2.dto.response.CustomerResponse;
import com.example.spring2.service.Impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(@RequestBody @Valid CustomerRequest request) {
        return ResponseEntity.ok(customerService.addCustomer(request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> getCustomersByPrefix(@RequestParam String prefix) {
        return ResponseEntity.ok(customerService.getCustomersByPrefix(prefix));
    }
}
