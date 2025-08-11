package com.example.spring2.service;

import com.example.spring2.dto.request.UpdateCartItemRequest;
import com.example.spring2.dto.request.UpdateCartRequest;
import com.example.spring2.dto.response.CartResponse;
import com.example.spring2.entity.Cart;
import com.example.spring2.entity.CartItem;
import com.example.spring2.entity.Product;
import com.example.spring2.exception.CartNotFoundException;
import com.example.spring2.exception.ProductNotFoundException;
import com.example.spring2.mapper.CartMapper;
import com.example.spring2.repository.CartRepository;
import com.example.spring2.repository.CustomerRepository;
import com.example.spring2.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartResponse getCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));
        return cartMapper.toCartResponse(cart);
    }

    /*@Transactional // hata aldığım metot
    public CartResponse updateCart(Long cartId, UpdateCartRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        // Sepetteki önceki ürünleri temizle
        cart.getItems().clear();

        List<CartItem> updatedItems = new ArrayList<>();

        for (UpdateCartItemRequest dto : request.getItems()) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(dto.getProductId()));

            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPriceAtAddition(product.getPrice()); // ürün sepete eklenirkenki fiyat

            updatedItems.add(item);
        }

        cart.setItems(updatedItems);

        // Toplam fiyatı hesapla
        double totalPrice = updatedItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
                .sum();
        cart.setTotalPrice(totalPrice);

        Cart updated = cartRepository.save(cart);

        return cartMapper.toCartResponse(updated);
    }*/

    @Transactional
    public CartResponse updateCart(Long cartId, UpdateCartRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        // Yeni ve güncel öğeleri tutacak geçici bir liste oluşturun.
        List<CartItem> newItems = new ArrayList<>();

        // İstekten gelen her bir öğe için CartItem nesnesini oluşturun.
        // items listesinin null olup olmadığını kontrol edin
        if (request.getItems() != null) {
            for (UpdateCartItemRequest dto : request.getItems()) {
                Product product = productRepository.findById(dto.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException(dto.getProductId()));

                CartItem item = new CartItem();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(dto.getQuantity());
                item.setPriceAtAddition(product.getPrice());

                newItems.add(item);
            }
        }

        // Önceki öğeleri temizle
        cart.getItems().clear();
        // Yeni öğeleri mevcut koleksiyona ekle
        cart.getItems().addAll(newItems);

        // Toplam fiyatı hesapla
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
                .sum();
        cart.setTotalPrice(totalPrice);

        Cart updated = cartRepository.save(cart);

        return cartMapper.toCartResponse(updated);
    }

    @Transactional
    public void emptyCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        cart.getItems().clear();
        cart.setTotalPrice(0.0); // toplam fiyatı da sıfırla

        cartRepository.save(cart);
    }

    @Transactional
    public CartResponse addProductToCart(Long customerId, Long productId, int quantity) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Ürün zaten sepette varsa miktarını artır
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setCart(cart);
            item.setPriceAtAddition(product.getPrice());// hata sonucu eklenen satır
            cart.getItems().add(item);
        }

        //toplam fiyatı yeniden hesapla
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
                .sum();
        cart.setTotalPrice(totalPrice);

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    /*@Transactional
    public CartResponse removeProductFromCart(Long customerId, Long productId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        Iterator<CartItem> iterator = cart.getItems().iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new ProductNotFoundException(productId); // Ürün sepette yoksa opsiyonel hata
        }

        // Toplam fiyatı güncelle
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
                .sum();
        cart.setTotalPrice(totalPrice);

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }*/

    /*@Transactional //aynı üründen birden fazla varsa hepsini siliyor.
    public CartResponse removeProductFromCart(Long customerId, Long productId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        // Belirtilen ürüne sahip CartItem'ı bul
        boolean isProductInCart = cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        // Ürün sepette yoksa hata fırlat
        if (!isProductInCart) {
            throw new ProductNotFoundException(productId);
        }

        // Toplam fiyatı güncelle
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
                .sum();
        cart.setTotalPrice(totalPrice);

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }*/

    @Transactional
    public CartResponse removeProductFromCart(Long customerId, Long productId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        // Ürün sepette var mı diye ara
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Quantity mantığı
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        } else {
            cart.getItems().remove(cartItem);
        }

        // Toplam fiyatı güncelle
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
                .sum();
        cart.setTotalPrice(totalPrice);

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }
}
