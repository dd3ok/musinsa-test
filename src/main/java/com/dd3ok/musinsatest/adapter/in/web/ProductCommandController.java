package com.dd3ok.musinsatest.adapter.in.web;

import com.dd3ok.musinsatest.adapter.in.web.request.ProductCreateRequest;
import com.dd3ok.musinsatest.adapter.in.web.request.ProductUpdateRequest;
import com.dd3ok.musinsatest.application.port.in.ProductCommandUseCase;
import com.dd3ok.musinsatest.application.port.in.command.ProductCreateCommand;
import com.dd3ok.musinsatest.application.port.in.command.ProductUpdateCommand;
import com.dd3ok.musinsatest.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final ProductCommandUseCase productCommandUseCase;

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductCreateRequest request) {
        ProductCreateCommand command = new ProductCreateCommand(request.getBrandId(), request.getCategory(), request.getPrice());
        Product createdProduct = productCommandUseCase.createProduct(command);
        URI location = URI.create("/api/v1/products/" + createdProduct.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest request) {
        ProductUpdateCommand command = new ProductUpdateCommand(productId, request.getBrandId(), request.getCategory(), request.getPrice());
        productCommandUseCase.updateProduct(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productCommandUseCase.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // COMMENT: 요구사항x 4번 테스트 후 조회를 위해 조회 추가
    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        List<Product> allProducts = productCommandUseCase.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }
}
