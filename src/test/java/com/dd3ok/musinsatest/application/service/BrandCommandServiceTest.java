package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.command.BrandCreateCommand;
import com.dd3ok.musinsatest.application.port.in.command.BrandUpdateCommand;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
import com.dd3ok.musinsatest.domain.brand.Brand;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandCommandServiceTest {

    @InjectMocks
    private BrandCommandService brandCommandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("성공: 유효한 이름으로 브랜드 생성을 요청하면 성공적으로 생성된다")
    void createBrand_Success() {
        // given
        BrandCreateCommand command = new BrandCreateCommand("NewBrand");
        given(brandRepository.findByName("NewBrand")).willReturn(Optional.empty());
        given(brandRepository.save(any(Brand.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Brand createdBrand = brandCommandService.createBrand(command);

        // then
        assertThat(createdBrand).isNotNull();
        assertThat(createdBrand.getName()).isEqualTo("NewBrand");
    }

    @Test
    @DisplayName("실패: 중복된 이름으로 브랜드 생성을 요청하면 BRAND_NAME_DUPLICATED 예외가 발생한다")
    void createBrand_Fail_WithDuplicateName() {
        // given
        BrandCreateCommand command = new BrandCreateCommand("ExistingBrand");
        Brand existingBrand = Brand.from(1L, "ExistingBrand");
        given(brandRepository.findByName("ExistingBrand")).willReturn(Optional.of(existingBrand));

        // when & then
        assertThatThrownBy(() -> brandCommandService.createBrand(command))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BRAND_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("성공: 브랜드 정보 수정을 요청하면 성공적으로 수정된다")
    void updateBrand_Success() {
        // given
        BrandUpdateCommand command = new BrandUpdateCommand(1L, "UpdatedName");
        Brand originalBrand = Brand.from(1L, "OriginalName");
        given(brandRepository.findById(1L)).willReturn(Optional.of(originalBrand));
        given(brandRepository.findByName("UpdatedName")).willReturn(Optional.empty());
        given(brandRepository.save(any(Brand.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Brand updatedBrand = brandCommandService.updateBrand(command);

        // then
        assertThat(updatedBrand.getId()).isEqualTo(1L);
        assertThat(updatedBrand.getName()).isEqualTo("UpdatedName");
    }

    @Test
    @DisplayName("실패: 다른 브랜드와 동일한 이름으로 수정을 요청하면 BRAND_NAME_DUPLICATED 예외가 발생한다")
    void updateBrand_Fail_WithDuplicateName() {
        // given
        BrandUpdateCommand command = new BrandUpdateCommand(1L, "ExistingName");
        Brand anotherBrand = Brand.from(2L, "ExistingName");

        given(brandRepository.findByName("ExistingName")).willReturn(Optional.of(anotherBrand));

        // when & then
        assertThatThrownBy(() -> brandCommandService.updateBrand(command))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BRAND_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("성공: 브랜드를 삭제 요청 시 정상적으로 삭제 된다")
    void 브랜드를_삭제_요청시_정상적으로_삭제된다() {
        // given
        long brandId = 1L;
        Brand mockBrand = Brand.from(brandId, "A");

        given(brandRepository.findById(brandId)).willReturn(Optional.of(mockBrand));
        given(productRepository.existsByBrandId(brandId)).willReturn(false); // 상품이 없음을 의미

        // when
        brandCommandService.deleteBrand(brandId);

        // then
        verify(brandRepository).delete(mockBrand);
    }

    @Test
    @DisplayName("실패: 상품이 존재하는 브랜드를 삭제 요청 시 BRAND_IN_USE 예외를 발생시킨다")
    void 사용중인_브랜드는_삭제에_실패한다() {
        // given
        long brandId = 1L;
        Brand mockBrand = Brand.from(brandId, "A");

        given(brandRepository.findById(brandId)).willReturn(Optional.of(mockBrand));
        when(productRepository.existsByBrandId(brandId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> brandCommandService.deleteBrand(brandId))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BRAND_IN_USE);
    }
}