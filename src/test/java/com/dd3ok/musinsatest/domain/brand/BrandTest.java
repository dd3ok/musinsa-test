package com.dd3ok.musinsatest.domain.brand;

import com.dd3ok.musinsatest.global.exception.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BrandTest {

    @Test
    @DisplayName("성공: 유효한 이름으로 Brand를 생성한다")
    void createBrand_Success() {
        // given
        String validName = "TestBrand";

        // when
        Brand brand = Brand.create(validName);

        // then
        assertThat(brand).isNotNull();
        assertThat(brand.name()).isEqualTo(validName);
    }

    @Test
    @DisplayName("실패: null 이름으로 Brand를 생성하면 BaseException이 발생한다")
    void createBrand_WithNullName_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> Brand.create(null))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("실패: 공백 이름으로 Brand를 생성하면 BaseException이 발생한다")
    void createBrand_WithBlankName_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> Brand.create("  "))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("성공: 유효한 이름으로 Brand 이름을 변경한다")
    void updateName_Success() {
        // given
        Brand brand = Brand.create("OriginalName");
        String newName = "UpdatedName";

        // when
        brand.updateName(newName);

        // then
        assertThat(brand.name()).isEqualTo(newName);
    }

    @Test
    @DisplayName("실패: null로 이름을 변경하면 BaseException이 발생한다")
    void updateName_WithNull_ThrowsException() {
        // given
        Brand brand = Brand.create("OriginalName");

        // when & then
        assertThatThrownBy(() -> brand.updateName(null))
                .isInstanceOf(BaseException.class);
    }
}