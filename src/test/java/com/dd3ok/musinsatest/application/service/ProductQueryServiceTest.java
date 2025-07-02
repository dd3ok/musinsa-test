package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.dto.BrandCategoryPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.BrandLowestPriceSetResult;
import com.dd3ok.musinsatest.application.port.in.dto.BrandPriceDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryLowestPriceProductDto;
import com.dd3ok.musinsatest.application.port.in.dto.CategoryPriceRangeResult;
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
        Brand brandA = Brand.from(1L, "A");
        Brand brandB = Brand.from(2L, "B");

        List<Product> mockProducts = List.of(
                Product.from(1L, new Price(new BigDecimal("10000")), Category.TOP, brandA),
                Product.from(2L, new Price(new BigDecimal("5000")), Category.PANTS, brandB)
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
        List<BrandCategoryPriceDto> mockLowestPrices = List.of(
                // A 브랜드 total 40000
                new BrandCategoryPriceDto(1L, Category.TOP, new BigDecimal("15000")),
                new BrandCategoryPriceDto(1L, Category.OUTER, new BigDecimal("25000")),
                // D 브랜드 total 36100 최저가
                new BrandCategoryPriceDto(4L, Category.TOP, new BigDecimal("10100")),
                new BrandCategoryPriceDto(4L, Category.OUTER, new BigDecimal("5100")),
                new BrandCategoryPriceDto(4L, Category.PANTS, new BigDecimal("5500")),
                new BrandCategoryPriceDto(4L, Category.BAG, new BigDecimal("15400")),
                // E 브랜드 total 36100 동일 최저가
                new BrandCategoryPriceDto(5L, Category.TOP, new BigDecimal("10000")),
                new BrandCategoryPriceDto(5L, Category.OUTER, new BigDecimal("6000")),
                new BrandCategoryPriceDto(5L, Category.PANTS, new BigDecimal("5100")),
                new BrandCategoryPriceDto(5L, Category.BAG, new BigDecimal("15000"))
        );

        // 2. 새로운 Repository 메서드를 given으로 설정
        given(productRepository.findLowestPriceByCategoryInBrand()).willReturn(mockLowestPrices);

        Brand winnerBrand = Brand.from(4L, "D");
        given(brandRepository.findById(4L)).willReturn(Optional.of(winnerBrand));

        // when
        BrandLowestPriceSetResult result = productQueryService.getLowestPriceProductsBrandSet();

        // then
        assertThat(result).isNotNull();
        assertThat(result.brandName()).isEqualTo("D");
        // D 브랜드의 총액(10100 + 5100 + 5500 + 15400)을 검증
        assertThat(result.totalPrice()).isEqualByComparingTo("36100");
        assertThat(result.products()).hasSize(4);
    }


    @Test
    @DisplayName("특정 카테고리의 최저가, 최고가 상품 조회가 성공한다")
    void 카테고리별_최저가_최고가_조회_성공() {
        // given
        String categoryName = "TOP";
        Category category = Category.TOP;

        Brand brandC = Brand.from(3L, "C");
        List<Product> lowestPriceProducts = List.of(
                Product.from(17L, new Price(new BigDecimal("10000")), category, brandC)
        );

        Brand brandI = Brand.from(9L, "I");
        List<Product> highestPriceProducts = List.of(
                Product.from(65L, new Price(new BigDecimal("11400")), category, brandI)
        );

        given(productRepository.findLowestPriceProductsByCategory(category)).willReturn(lowestPriceProducts);
        given(productRepository.findHighestPriceProductsByCategory(category)).willReturn(highestPriceProducts);

        // when
        CategoryPriceRangeResult result = productQueryService.getCategoryLowAndHighPrice(categoryName);

        // then
        assertThat(result).isNotNull();
        assertThat(result.category()).isEqualTo(category.getDescription());

        assertThat(result.lowestPriceProducts()).hasSize(1);
        BrandPriceDto lowestDto = result.lowestPriceProducts().getFirst();
        assertThat(lowestDto.brandName()).isEqualTo("C");
        assertThat(lowestDto.price()).isEqualByComparingTo("10000");

        assertThat(result.highestPriceProducts()).hasSize(1);
        BrandPriceDto highestDto = result.highestPriceProducts().getFirst();
        assertThat(highestDto.brandName()).isEqualTo("I");
        assertThat(highestDto.price()).isEqualByComparingTo("11400");
    }
}
