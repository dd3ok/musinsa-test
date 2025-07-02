package com.dd3ok.musinsatest.adapter.in.web;

import com.dd3ok.musinsatest.adapter.in.web.response.BrandLowestPriceSetResponse;
import com.dd3ok.musinsatest.adapter.in.web.response.CategoryLowestPriceResponse;
import com.dd3ok.musinsatest.adapter.in.web.response.CategoryPriceRangeResponse;
import com.dd3ok.musinsatest.application.port.in.BrandLowestPriceSetUseCase;
import com.dd3ok.musinsatest.application.port.in.CategoryLowHighPriceUseCase;
import com.dd3ok.musinsatest.application.port.in.LowestPriceProductsUseCase;
import com.dd3ok.musinsatest.application.port.in.dto.BrandLowestPriceSetResult;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryPriceRangeResult;
import com.dd3ok.musinsatest.application.port.in.dto.LowestPriceProductsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductQueryController {

    private final LowestPriceProductsUseCase lowestPriceProductsUseCase;
    private final BrandLowestPriceSetUseCase brandLowestPriceSetUseCase;
    private final CategoryLowHighPriceUseCase categoryLowHighPriceUseCase;

    // API 1번 카테고리별 최저가 조회
    @GetMapping("/lowest-price/group-category")
    public ResponseEntity<CategoryLowestPriceResponse> getLowestPriceProductsByCategory() {
        LowestPriceProductsResult serviceResponse = lowestPriceProductsUseCase.getLowestPriceProductsByCategory();
        
        CategoryLowestPriceResponse webResponse = CategoryLowestPriceResponse.from(serviceResponse);
        
        return ResponseEntity.ok(webResponse);
    }

    // API 2번 모든 카테고리 단일브랜드 최저가
    @GetMapping("/lowest-price/brand-set")
    public ResponseEntity<BrandLowestPriceSetResponse> getLowestPriceBrandSet() {
        BrandLowestPriceSetResult serviceResponse = brandLowestPriceSetUseCase.getLowestPriceProductsBrandSet();
        return ResponseEntity.ok(BrandLowestPriceSetResponse.from(serviceResponse));
    }

    // API 3번 카테고리 이름으로 최저가, 최고가 브랜드, 가격 조회
    @GetMapping("/categories/{categoryName}/low-high-price")
    public ResponseEntity<CategoryPriceRangeResponse> getCategoryLowAndHighPrice(@PathVariable String categoryName) {
        CategoryPriceRangeResult serviceResponse = categoryLowHighPriceUseCase.getCategoryLowAndHighPrice(categoryName);
        return ResponseEntity.ok(CategoryPriceRangeResponse.from(serviceResponse));
    }
}