package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.dto.BrandLowestPriceSetResult;
import com.dd3ok.musinsatest.application.port.in.dto.BrandTotalPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryLowestPriceProductDto;
import com.dd3ok.musinsatest.application.port.in.dto.LowestPriceProductsResult;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Price;
import com.dd3ok.musinsatest.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {

    @InjectMocks
    private ProductQueryService productQueryService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Test
    @DisplayName("카테고리별 최저가 상품 목록 조회 시, 총액과 상품 목록 리턴")
    void 카테고리별_최저가_상품목록_조회시_총액과_상품목록을_정확히_반환한다() {
        // given
        Brand brandA = new Brand(1L, "A");
        Brand brandB = new Brand(2L, "B");

        List<Product> mockProducts = List.of(
                new Product(1L, new Price(new BigDecimal("10000")), Category.TOP, brandA),
                new Product(2L, new Price(new BigDecimal("5000")), Category.PANTS, brandB)
        );

        given(productRepository.findLowestPriceProductsGroupByCategory()).willReturn(mockProducts);

        // when
        LowestPriceProductsResult result = productQueryService.getLowestPriceProductsByCategory();

        // then
        assertThat(result).isNotNull();
        assertThat(result.totalPrice()).isEqualByComparingTo("15000");
        assertThat(result.products()).hasSize(2);

        Set<CategoryLowestPriceProductDto> expectedProducts = Set.of(
                new CategoryLowestPriceProductDto(Category.TOP, "A", new BigDecimal("10000")),
                new CategoryLowestPriceProductDto(Category.PANTS, "B", new BigDecimal("5000"))
        );

        Set<CategoryLowestPriceProductDto> actualProducts = Set.copyOf(result.products());

        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Test
    @DisplayName("단일 브랜드 최저가 세트 조회 시, 여러 브랜드가 최저가일 경우 ID가 가장 빠른 브랜드 리턴")
    void 단일브랜드_최저가_세트_조회_성공() {
        // given
        // 총액이 36100원으로 동일하지만, brandId가 다른 브랜드 포함
        var winnerCandidate = new BrandTotalPriceDto(4L, new BigDecimal("36100"));
        var tieCandidate = new BrandTotalPriceDto(5L, new BigDecimal("36100"));
        var otherCandidate = new BrandTotalPriceDto(1L, new BigDecimal("40000"));

        List<BrandTotalPriceDto> mockCandidates = List.of(winnerCandidate, tieCandidate, otherCandidate);

        // 최종 선택될 브랜드와 상품 목록
        Brand winnerBrand = new Brand(4L, "D");
        List<Product> winnerProducts = List.of(
                new Product(25L, new Price(new BigDecimal("10100")), Category.TOP, winnerBrand),
                new Product(26L, new Price(new BigDecimal("5100")), Category.OUTER, winnerBrand)
        );

        // 2. 최저가 브랜드 세팅
        given(productRepository.findBrandWithLowestTotalPrice()).willReturn(mockCandidates);
        given(brandRepository.findById(4L)).willReturn(Optional.of(winnerBrand));
        given(productRepository.findAllByBrandId(4L)).willReturn(winnerProducts);

        // when
        BrandLowestPriceSetResult result = productQueryService.getLowestPriceProductsBrandSet();

        // then
        assertThat(result).isNotNull();
        assertThat(result.brandName()).isEqualTo("D");
        assertThat(result.totalPrice()).isEqualByComparingTo("36100");
        assertThat(result.products()).hasSize(winnerProducts.size());
    }
}