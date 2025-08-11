package com.example.spring2.mapper;

import com.example.spring2.dto.request.CreateProductRequest;
import com.example.spring2.dto.request.UpdateProductRequest;
import com.example.spring2.dto.response.ProductResponse;
import com.example.spring2.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        return product;
    }

    public void updateProduct(Product product, UpdateProductRequest request) {
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getDescription()
        );
    }
}
