package com.example.spring2.service;

import com.example.spring2.dto.request.PlaceOrderRequest;
import com.example.spring2.dto.response.OrderResponse;
import com.example.spring2.entity.*;
import com.example.spring2.exception.CustomerNotFoundException;
import com.example.spring2.exception.OrderNotFoundException;
import com.example.spring2.exception.ProductNotFoundException;
import com.example.spring2.mapper.OrderMapper;
import com.example.spring2.repository.CustomerRepository;
import com.example.spring2.repository.OrderRepository;
import com.example.spring2.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    /*@Transactional //hatalı kod
    public OrderResponse placeOrder(Long customerId, PlaceOrderRequest orderRequestDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Order order = new Order();
        order.setOrderCode(UUID.randomUUID().toString());
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());

        double totalPrice = 0.0;

        for (var itemDto : orderRequestDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(itemDto.getProductId()));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            // stock güncelle
            product.setStock(product.getStock() - itemDto.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchaseTime(product.getPrice());

            ProductPriceHistory priceHistory = new ProductPriceHistory();
            priceHistory.setProduct(product);
            priceHistory.setPrice(product.getPrice());
            priceHistory.setChangedAt(LocalDateTime.now());
            product.getPriceHistoryList().add(priceHistory);

            order.getOrderItems().add(orderItem);

            totalPrice += product.getPrice() * itemDto.getQuantity();
            order.getOrderItems().add(orderItem);
        }

        order.setTotalPrice(totalPrice);

        cartService.emptyCart(customerId); // sepeti temizler.

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponse(savedOrder);
    }*/

    @Transactional
    public OrderResponse placeOrder(Long customerId, PlaceOrderRequest orderRequestDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Order order = new Order();
        order.setOrderCode(UUID.randomUUID().toString());
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());

        //Gelen ürünleri productId'ye göre grupla ve miktarları topla
        Map<Long, Integer> consolidatedItems = orderRequestDto.getItems().stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProductId(), // productId'ye göre grupla
                        Collectors.summingInt(item -> item.getQuantity()) // Miktarları topla
                ));

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        //Birleştirilmiş ürün listesi üzerinde döngü yap
        for (Map.Entry<Long, Integer> entry : consolidatedItems.entrySet()) {
            Long productId = entry.getKey();
            Integer totalQuantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            // Stok kontrolünü toplam miktar üzerinden yap
            if (product.getStock() < totalQuantity) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() + ". Required: " + totalQuantity + ", Available: " + product.getStock());
            }

            // Stoğu bir kerede güncelle
            product.setStock(product.getStock() - totalQuantity);

            // OrderItem nesnesini oluştur
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(totalQuantity);
            orderItem.setPriceAtPurchaseTime(product.getPrice());

            // Fiyat geçmişi kaydını ekle
            ProductPriceHistory priceHistory = new ProductPriceHistory();
            priceHistory.setProduct(product);
            priceHistory.setPrice(product.getPrice());
            priceHistory.setChangedAt(LocalDateTime.now());
            product.getPriceHistoryList().add(priceHistory);

            // OrderItem'ı listeye ekle
            orderItems.add(orderItem);

            totalPrice += product.getPrice() * totalQuantity;
        }

        // Toplam fiyatı ve sipariş kalemlerini ata
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        // Sepeti temizle
        cartService.emptyCart(customerId);

        // Siparişi kaydet
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponse(savedOrder);
    }

    public OrderResponse getOrderForCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode);

        if (order == null) {
            throw new OrderNotFoundException(orderCode);
        }

        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getAllOrdersForCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        List<Order> orders = orderRepository.findAllByCustomerId(customerId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
}