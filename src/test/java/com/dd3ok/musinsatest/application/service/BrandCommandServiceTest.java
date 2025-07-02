package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Price;
import com.dd3ok.musinsatest.domain.product.Product;
import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrandCommandServiceTest {

    @InjectMocks
    private BrandCommandService brandAdminService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("성공: 브랜드를 삭제 요청 시 정상적으로 삭제 된다")
    void 브랜드를_삭제_요청시_정상적으로_삭제된다() {
        // given
        long brandId = 1L;
        Brand mockBrand = new Brand(brandId, "A");

        given(brandRepository.findById(brandId)).willReturn(Optional.of(mockBrand));
        given(productRepository.existsByBrandId(brandId)).willReturn(false); // 상품이 없음을 의미

        // when
        brandAdminService.deleteBrand(brandId);

        // then
        // brandRepository의 delete 메서드가 mockBrand 객체를 인자로 받아 호출되었는지 검증
        verify(brandRepository).delete(mockBrand);
    }

    @Test
    @DisplayName("실패: 상품이 존재하는 브랜드를 삭제 요청 시 BRAND_IN_USE 예외를 발생시킨다")
    void 사용중인_브랜드는_삭제에_실패한다() {
        // given
        long brandId = 1L;
        Brand mockBrand = new Brand(brandId, "A");
        Product mockProduct = new Product(1L, new Price(BigDecimal.valueOf(10000)), Category.BAG, mockBrand);

        given(brandRepository.findById(brandId)).willReturn(Optional.of(mockBrand));
        given(productRepository.existsByBrandId(brandId)).willReturn(true);

        // when & then
        // brandAdminService.deleteBrand(brandId)를 실행했을 때,
        // CustomException이 발생하고, 그 예외의 ErrorCode가 BRAND_IN_USE인지 검증
        assertThatThrownBy(() -> brandAdminService.deleteBrand(brandId))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BRAND_IN_USE);
    }
}
