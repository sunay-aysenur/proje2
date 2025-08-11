package com.example.spring2.controller;

import com.example.spring2.dto.request.CreateProductRequest;
import com.example.spring2.dto.request.UpdateProductRequest;
import com.example.spring2.dto.response.ProductResponse;
import com.example.spring2.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //Bu sınıfın bir RESTful API Controller olduğunu belirtir.
@RequestMapping("/api/products") //HTTP isteklerini belirli bir URL yoluna (endpoint) yönlendirmek için kullanılır.
@RequiredArgsConstructor // Lombok sayesinde final olarak tanımlanan productService için otomatik olarak bir constructor oluşturulur, böylece dependency injection sağlanır.
public class ProductController {

    private final ProductService productService;

    @PostMapping // Bu genellikle yeni bir kaynak (örneğin, yeni bir ürün) oluşturmak için kullanılır.
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) { //Gelen HTTP isteğinin gövdesindeki JSON verisinin otomatik olarak CreateProductRequest nesnesine dönüştürülmesini sağlar.
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @GetMapping //Bu bir REST API endpoint'idir. HTTP GET isteği geldiğinde tüm ürünleri liste olarak döndürür.
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}") //"/{id}" kısmı, URL içinde bir path variable olduğunu gösterir.
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) { //@PathVariable, URL yolundaki (/{id}) değeri alır ve metoda parametre olarak verir.
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}") //Bu metot, REST API’de bir ürünün ID’sine göre güncellenmesini sağlar.
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}") //Bu metot, REST API'de bir ürünü ID’ye göre silmek için kullanılır.
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); //ResponseEntity.noContent() HTTP 204 No Content durum kodunu belirtir.
                                                   //Bu durum kodu, isteğin başarılı olduğunu ama geri dönecek içerik olmadığını ifade eder.
                                                   //build() metodu, bu yanıtı oluşturur ve döner.
    }
}
