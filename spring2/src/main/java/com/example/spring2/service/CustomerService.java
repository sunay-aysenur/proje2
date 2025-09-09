package com.example.spring2.service;

import com.example.spring2.dto.request.CustomerRequest;
import com.example.spring2.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse addCustomer(CustomerRequest request);
    CustomerResponse getCustomer(Long customerId);
    List<CustomerResponse> getCustomersByPrefix(String prefix);
}
