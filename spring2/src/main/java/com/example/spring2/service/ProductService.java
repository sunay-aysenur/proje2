package com.example.spring2.service;

import com.example.spring2.dto.request.CreateProductRequest;
import com.example.spring2.dto.request.UpdateProductRequest;
import com.example.spring2.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
    void increaseStock(Long productId, int quantity);
}
