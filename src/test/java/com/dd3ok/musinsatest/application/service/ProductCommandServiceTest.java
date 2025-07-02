package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.command.ProductCreateCommand;
import com.dd3ok.musinsatest.application.port.in.command.ProductUpdateCommand;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductCommandServiceTest {

    @InjectMocks
    private ProductCommandService productCommandService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Test
    @DisplayName("성공: 유효한 요청으로 상품 생성을 시도하면 상품이 생성된다")
    void createProduct_Success() {
        // given
        ProductCreateCommand command = new ProductCreateCommand(1L, "TOP", 15000);
        Brand mockBrand = Brand.from(1L, "A");
        given(brandRepository.findById(1L)).willReturn(Optional.of(mockBrand));
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Product createdProduct = productCommandService.createProduct(command);

        // then
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getBrand().getName()).isEqualTo("A");
        assertThat(createdProduct.getCategory()).isEqualTo(Category.TOP);
        assertThat(createdProduct.getPrice().value()).isEqualByComparingTo("15000");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 브랜드 ID로 상품 생성을 시도하면 BRAND_NOT_FOUND 예외가 발생한다")
    void createProduct_Fail_WithNonExistentBrand() {
        // given
        ProductCreateCommand command = new ProductCreateCommand(999L, "TOP", 15000);
        given(brandRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productCommandService.createProduct(command))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BRAND_NOT_FOUND);
    }

    @Test
    @DisplayName("실패: 유효하지 않은 카테고리 이름으로 상품 생성을 시도하면 CATEGORY_NOT_FOUND 예외가 발생한다")
    void createProduct_Fail_WithInvalidCategory() {
        // given
        ProductCreateCommand command = new ProductCreateCommand(1L, "INVALID_CATEGORY", 15000);
        Brand mockBrand = Brand.from(1L, "A");
        given(brandRepository.findById(1L)).willReturn(Optional.of(mockBrand));

        // when and then
        assertThatThrownBy(() -> productCommandService.createProduct(command))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    @DisplayName("성공: 상품 정보 수정을 요청하면 성공적으로 수정된다")
    void updateProduct_Success() {
        // given
        Long productId = 1L;
        ProductUpdateCommand command = new ProductUpdateCommand(productId, 2L, "PANTS", 99000);
        Brand brandA = Brand.from(1L, "A");
        Brand brandB = Brand.from(2L, "B");
        Product originalProduct = Product.from(productId, new Price(new BigDecimal("10000")), Category.TOP, brandA);

        given(productRepository.findById(productId)).willReturn(Optional.of(originalProduct));
        given(brandRepository.findById(2L)).willReturn(Optional.of(brandB));
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Product updatedProduct = productCommandService.updateProduct(command);

        // then
        assertThat(updatedProduct.getId()).isEqualTo(productId);
        assertThat(updatedProduct.getBrand().getName()).isEqualTo("B");
        assertThat(updatedProduct.getCategory()).isEqualTo(Category.PANTS);
        assertThat(updatedProduct.getPrice().value()).isEqualByComparingTo("99000");
    }

    @Test
    @DisplayName("성공: 상품을 성공적으로 삭제한다")
    void deleteProduct_Success() {
        // given
        Long productId = 1L;
        Brand brand = Brand.from(1L, "A");
        Product product = Product.from(productId, new Price(new BigDecimal("10000")), Category.TOP, brand);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productCommandService.deleteProduct(productId);

        // then
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 상품 ID로 삭제를 시도하면 PRODUCT_NOT_FOUND 예외가 발생한다")
    void deleteProduct_Fail_WithNonExistentProduct() {
        // given
        Long invalidProductId = 999L;
        given(productRepository.findById(invalidProductId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productCommandService.deleteProduct(invalidProductId))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PRODUCT_NOT_FOUND);
    }
}