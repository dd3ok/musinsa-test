package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.BrandLowestPriceSetUseCase;
import com.dd3ok.musinsatest.application.port.in.CategoryLowHighPriceUseCase;
import com.dd3ok.musinsatest.application.port.in.LowestPriceProductsUseCase;
import com.dd3ok.musinsatest.application.port.in.dto.BrandCategoryPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.BrandLowestPriceSetResult;
import com.dd3ok.musinsatest.application.port.in.dto.BrandPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryLowestPriceProductDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryPriceRangeResult;
import com.dd3ok.musinsatest.application.port.in.dto.LowestPriceProductsResult;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
import com.dd3ok.musinsatest.domain.brand.Brand;
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
public class ProductQueryService implements LowestPriceProductsUseCase, BrandLowestPriceSetUseCase,
        CategoryLowHighPriceUseCase {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;


    @Override
    public LowestPriceProductsResult getLowestPriceProductsByCategory() {
        // 1. 최저가 조회
        List<Product> products = productRepository.findLowestPriceProductsGroupByCategory();

        // COMMENT: 같은 가격일 경우 브랜드 이름이 빠른순 선택 했습니다.
        // 2. 카테고리별로 하나의 상품만 선택
        Map<Category, Product> lowestPriceProductMap = products.stream()
                .collect(Collectors.toMap(
                        Product::getCategory,
                        p -> p,
                        (p1, p2) -> p1.getBrand().getName().compareTo(p2.getBrand().getName()) < 0 ? p1 : p2
                ));

        List<CategoryLowestPriceProductDto> productDtos = lowestPriceProductMap.values().stream()
                .map(product -> new CategoryLowestPriceProductDto(
                        product.getCategory(),
                        product.getBrand().getName(),
                        product.getPrice().value()
                ))
                .toList();

        // 3. 총액 계산
        BigDecimal totalPrice = productDtos.stream()
                .map(CategoryLowestPriceProductDto::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LowestPriceProductsResult(productDtos, totalPrice);
    }

    @Override
    public BrandLowestPriceSetResult getLowestPriceProductsBrandSet() {
        // 1. 브랜드별 최저가 조회
        List<BrandCategoryPriceDto> lowestPrices = productRepository.findLowestPriceByCategoryInBrand();

        // 2. 가져온 최저가 목록을 브랜드별로 그룹화하고, 각 브랜드의 최저가 총합을 계산합니다.
        Map<Long, BigDecimal> brandTotalPriceMap = lowestPrices.stream()
                .collect(Collectors.groupingBy(
                        BrandCategoryPriceDto::brandId,
                        Collectors.mapping(
                                BrandCategoryPriceDto::price,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // 3. 총합이 가장 낮은 브랜드를 찾습니다. (가격이 같으면 브랜드 ID가 낮은 순)
        Long winnerBrandId = brandTotalPriceMap.entrySet().stream()
                .min(Map.Entry.<Long, BigDecimal>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        // 4. 최종 결과를 구성합니다.
        Brand winnerBrand = brandRepository.findById(winnerBrandId)
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        List<CategoryPriceDto> categoryPriceDtos = lowestPrices.stream()
                .filter(dto -> dto.brandId().equals(winnerBrandId))
                .map(dto -> new CategoryPriceDto(dto.category(), dto.price()))
                .toList();

        BigDecimal totalPrice = brandTotalPriceMap.get(winnerBrandId);

        return new BrandLowestPriceSetResult(
                winnerBrand.getName(),
                categoryPriceDtos,
                totalPrice
        );
    }

    @Override
    public CategoryPriceRangeResult getCategoryLowAndHighPrice(String categoryName) {
        Category category = Category.fromString(categoryName.toUpperCase());

        List<Product> lowestProducts = productRepository.findLowestPriceProductsByCategory(category);
        List<Product> highestProducts = productRepository.findHighestPriceProductsByCategory(category);

        List<BrandPriceDto> lowestPriceDtos = lowestProducts.stream()
                .map(p -> new BrandPriceDto(p.getBrand().getName(), p.getPrice().value()))
                .toList();

        List<BrandPriceDto> highestPriceDtos = highestProducts.stream()
                .map(p -> new BrandPriceDto(p.getBrand().getName(), p.getPrice().value()))
                .toList();

        return new CategoryPriceRangeResult(category.getDescription(), lowestPriceDtos, highestPriceDtos);
    }
}