package com.example.spring2.service.Impl;

import com.example.spring2.dto.request.CreateProductRequest;
import com.example.spring2.dto.request.UpdateProductRequest;
import com.example.spring2.dto.response.ProductResponse;
import com.example.spring2.entity.Product;
import com.example.spring2.exception.ProductNotFoundException;
import com.example.spring2.mapper.ProductMapper;
import com.example.spring2.repository.ProductRepository;
import com.example.spring2.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository; //Veritabanı işlemleri (CRUD).
    private final ProductMapper productMapper; //DTO <-> Entity dönüşümleri.

    @Override
    public ProductResponse createProduct(CreateProductRequest request) { //API üzerinden gelen ürün oluşturma isteği.
        Product product = productMapper.toProduct(request); //DTO'dan Product entity'e dönüştürür.
        Product savedProduct = productRepository.save(product); //Ürünü veritabanına kaydeder.
        return productMapper.toProductResponse(savedProduct); //Kaydedilen ürünü DTO formatına çevirip döner.
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream() //Tüm ürünleri veritabanından çeker. Liste üzerinde stream işlemi başlatır.
                .map(productMapper::toProductResponse) //Her bir entity’yi ProductResponse’a dönüştürür.
                .collect(Collectors.toList()); // Sonuçları liste olarak döner.
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toProductResponse(product); //Bulunan entity DTO’ya dönüştürülür.
    }

    @Override
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productMapper.updateProduct(product, request); //Mevcut ürün entity'si, gelen UpdateProductRequest verileri ile güncellenir.

        Product updatedProduct = productRepository.save(product); //Güncellenmiş ürün tekrar veritabanına kaydedilir.
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void increaseStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }
}
