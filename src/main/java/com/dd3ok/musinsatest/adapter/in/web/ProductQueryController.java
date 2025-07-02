package com.dd3ok.musinsatest.adapter.in.web;

import com.dd3ok.musinsatest.adapter.in.web.response.CategoryLowestPriceResponse;
import com.dd3ok.musinsatest.application.port.in.LowestPriceProductsUseCase;
import com.dd3ok.musinsatest.application.port.in.dto.LowestPriceProductsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductQueryController {

    private final LowestPriceProductsUseCase lowestPriceProductsUseCase;

    // API 1번 카테고리별 최저가 조회
    @GetMapping("/lowest-price/group-category")
    public ResponseEntity<CategoryLowestPriceResponse> getLowestPriceProductsByCategory() {
        LowestPriceProductsResult serviceResponse = lowestPriceProductsUseCase.getLowestPriceProductsByCategory();
        
        CategoryLowestPriceResponse webResponse = CategoryLowestPriceResponse.from(serviceResponse);
        
        return ResponseEntity.ok(webResponse);
    }
}
