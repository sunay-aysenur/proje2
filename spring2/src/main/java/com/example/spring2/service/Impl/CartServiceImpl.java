package com.example.spring2.service.Impl;

import com.example.spring2.dto.request.UpdateCartItemRequest;
import com.example.spring2.dto.request.UpdateCartRequest;
import com.example.spring2.dto.response.CartItemResponse;
import com.example.spring2.dto.response.CartResponse;
import com.example.spring2.dto.response.CartSummaryResponse;
import com.example.spring2.entity.Cart;
import com.example.spring2.entity.CartItem;
import com.example.spring2.entity.Product;
import com.example.spring2.exception.CartNotFoundException;
import com.example.spring2.exception.ProductNotFoundException;
import com.example.spring2.mapper.CartMapper;
import com.example.spring2.repository.CartRepository;
import com.example.spring2.repository.CustomerRepository;
import com.example.spring2.repository.ProductRepository;
import com.example.spring2.service.CartService;
import com.example.spring2.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final ProductServiceImpl productServiceImpl;

    @Override
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

    @Override
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

    @Override
    @Transactional
    public void emptyCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        cart.getItems().clear();
        cart.setTotalPrice(0.0); // toplam fiyatı da sıfırla

        cartRepository.save(cart);
    }

    @Override
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

    /*@Transactional //İlk bulduğu productId eşleşen öğeyi siler. break sayesinde tek bir öğe silinir
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

    /*@Transactional
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

    /*@Override
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
    }*/

    @Override// aynı productId sine sahip olan ürünleri quantity mantığına göre azaltarak siler ve stok kontrolü yapar.
    @Transactional
    public CartResponse removeProductFromCart(Long customerId, Long productId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        // Ürün sepette var mı diye ara
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Quantity mantığı + stok güncelleme
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            productServiceImpl.increaseStock(productId, 1);
        } else {
            cart.getItems().remove(cartItem);
            // Sepetten tamamen çıkar
            cart.removeCartItem(cartItem); // JPA ilişkisini koparır
            productServiceImpl.increaseStock(productId, cartItem.getQuantity());
        }
        // Toplam fiyatı güncelle
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
                .sum();
        cart.setTotalPrice(totalPrice);

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    /*@Override //aynı productId ye sahip ürünlerin hepsini çıkarır ve stok kontrolünü sağlar
    @Transactional
    public CartResponse removeProductFromCart(Long customerId, Long productId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        boolean removed = false;

        Iterator<CartItem> iterator = cart.getItems().iterator();
    while (iterator.hasNext()) {
        CartItem item = iterator.next();
        if (item.getProduct().getId().equals(productId)) {
          // Stoğu geri artır
          productService.increaseStock(productId, item.getQuantity());

          // JPA ilişkisini kopar
            cart.removeCartItem(item);

            removed = true;
            break; // Aynı üründen sadece tek satır varsa break mantıklı
        }
    }

    if (!removed) {
        throw new ProductNotFoundException(productId);
    }

    double totalPrice = cart.getItems().stream()
            .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
            .sum();
    cart.setTotalPrice(totalPrice);

     return cartMapper.toCartResponse(cartRepository.save(cart));
     }
     */

    /*@Override //Tüm aynı ürünleri siler. quantity yok.
    @Transactional
    public CartResponse removeProductFromCart(Long customerId, Long productId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        // ürünleri sil ve stok iade et
        List<CartItem> itemsToRemove = new ArrayList<>();
        boolean isProductInCart = cart.getItems().removeIf(item -> {
            if (item.getProduct().getId().equals(productId)) {
                productService.increaseStock(productId, item.getQuantity()); // stoğu geri ekle
                itemsToRemove.add(item); // JPA ilişkisi için
                return true; // listeden çıkar
            }
            return false;
        });

        // Ürün sepette yoksa hata fırlat
        if (!isProductInCart) {
            throw new ProductNotFoundException(productId);
        }

        // JPA ilişkisini temizle
        itemsToRemove.forEach(cart::removeCartItem);

        // Toplam fiyatı güncelle
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtAddition())
                .sum();
        cart.setTotalPrice(totalPrice);

        // Kaydet ve güncel sepeti döndür
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }*/

    @Override
    public CartSummaryResponse getCartSummary(Long customerId) {
        CartResponse cart = getCart(customerId); // mevcut methodla sepeti çekiyoruz

        int totalItems = cart.getItems().stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        int distinctProducts = cart.getItems().size();

        double totalPrice = cart.getItems().stream()
                .mapToDouble(CartItemResponse::getTotalPrice)
                .sum();

        return CartSummaryResponse.builder()
                .customerId(customerId)
                .totalItems(totalItems)
                .distinctProducts(distinctProducts)
                .totalPrice(totalPrice) // CartSummaryResponse.totalPrice alanı da Double olmalı
                .build();
    }
}
