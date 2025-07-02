package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.LowestPriceProductsUseCase;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryLowestPriceProductDto;
import com.dd3ok.musinsatest.application.port.in.dto.LowestPriceProductsResult;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService implements LowestPriceProductsUseCase {

    private final ProductRepository productRepository;

    @Override
    public LowestPriceProductsResult getLowestPriceProductsByCategory() {
        // 1. 최저가 조회
        List<Product> products = productRepository.findLowestPriceProductsGroupByCategory();

        // COMMENT: 같은 가격일 경우 브랜드 이름이 빠른순 선택 했습니다.
        // 2. 카테고리별로 하나의 상품만 선택
        Map<Category, Product> lowestPriceProductMap = products.stream()
                .collect(Collectors.toMap(
                        Product::category,
                        p -> p,
                        (p1, p2) -> p1.brand().name().compareTo(p2.brand().name()) < 0 ? p1 : p2
                ));

        // 3. dto 매핑
        List<CategoryLowestPriceProductDto> productDtos = lowestPriceProductMap.values().stream()
                .map(product -> new CategoryLowestPriceProductDto(
                        product.category(),
                        product.brand().name(),
                        product.price().value()
                ))
                .toList();

        // 4. 총액 계산
        BigDecimal totalPrice = productDtos.stream()
                .map(CategoryLowestPriceProductDto::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LowestPriceProductsResult(productDtos, totalPrice);
    }
}