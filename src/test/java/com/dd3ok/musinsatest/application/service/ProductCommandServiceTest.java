package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.command.ProductCreateCommand;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
    void 유효한_정보로_상품생성을_요청하면_성공적으로_생성된다() {
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
    }

    @Test
    @DisplayName("실패: 존재하지 않는 브랜드 ID로 상품 생성을 시도하면 BRAND_NOT_FOUND 예외가 발생한다")
    void 존재하지_않는_브랜드ID로_상품생성을_요청하면_예외가_발생한다() {
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
    void 유효하지_않은_카테고리명으로_상품생성을_요청하면_예외가_발생한다() {
        // given
        ProductCreateCommand command = new ProductCreateCommand(1L, "INVALID_CATEGORY", 15000);
        Brand mockBrand = Brand.from(1L, "A");
        given(brandRepository.findById(1L)).willReturn(Optional.of(mockBrand));

        // when and then
        assertThatThrownBy(() -> productCommandService.createProduct(command))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CATEGORY_NOT_FOUND);
    }
}
