package com.example.spring2.controller;

import com.example.spring2.dto.request.AddProductToCartRequest;
import com.example.spring2.dto.request.RemoveProductFromCartRequest;
import com.example.spring2.dto.request.UpdateCartRequest;
import com.example.spring2.dto.response.CartResponse;
import com.example.spring2.dto.response.CartSummaryResponse;
import com.example.spring2.service.Impl.CartServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    @PostMapping("/{customerId}/add")
    public ResponseEntity<CartResponse> addProductToCart(
            @PathVariable Long customerId,
            @RequestBody @Valid AddProductToCartRequest request) {
        return ResponseEntity.ok(
                cartService.addProductToCart(customerId, request.getProductId(), request.getQuantity()));
    }

    @PutMapping("/{cartId}") //PUT, genellikle bir kaynağı (resource) tamamen güncellemek için kullanılır.
    public ResponseEntity<CartResponse> updateCart(@PathVariable Long cartId, @RequestBody @Valid UpdateCartRequest request) {
        return ResponseEntity.ok(cartService.updateCart(cartId, request));
    }

    @DeleteMapping("/{customerId}/empty")
    public ResponseEntity<Void> emptyCart(@PathVariable Long customerId) {
        cartService.emptyCart(customerId);
        return ResponseEntity.noContent().build();
    }

    /*@DeleteMapping("/{customerId}/remove") //@RequestParam kullanıldığından dto lu bir yapı kullanılmadı. Bu sebeple bu yapıyı aşağıdaki gibi değiştirdim.
    public ResponseEntity<CartResponse> removeProductFromCart(@PathVariable Long customerId,
                                                              @RequestParam Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(customerId, productId));
    }*/

    @DeleteMapping("/{customerId}/remove")
    public ResponseEntity<CartResponse> removeProductFromCart(
            @PathVariable Long customerId,
            @RequestBody @Valid RemoveProductFromCartRequest request) {
        return ResponseEntity.ok(cartService.removeProductFromCart(customerId, request.getProductId()));
    }

    @GetMapping("/{customerId}/summary")
    public ResponseEntity<CartSummaryResponse> getCartSummary(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.getCartSummary(customerId));
    }

}
