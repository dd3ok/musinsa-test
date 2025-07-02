package com.dd3ok.musinsatest.domain.product;

import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    private Price validPrice;
    private Category validCategory;
    private Brand validBrand;

    @BeforeEach
    void setUp() {
        validPrice = new Price(new BigDecimal("10000"));
        validCategory = Category.TOP;
        validBrand = Brand.create("TestBrand");
    }

    @Test
    @DisplayName("성공: 유효한 값들로 Product를 생성한다")
    void createProduct_Success() {
        // when
        Product product = Product.create(validPrice, validCategory, validBrand);

        // then
        assertThat(product).isNotNull();
        assertThat(product.price()).isEqualTo(validPrice);
        assertThat(product.category()).isEqualTo(validCategory);
        assertThat(product.brand()).isEqualTo(validBrand);
    }

    @Test
    @DisplayName("실패: Price가 null이면 Exception이 발생한다")
    void createProduct_WithNullPrice_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> Product.create(null, validCategory, validBrand))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PRICE_NULL);
    }

    @Test
    @DisplayName("실패: Category가 null이면 Exception이 발생한다")
    void createProduct_WithNullCategory_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> Product.create(validPrice, null, validBrand))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_CATEGORY_NULL);
    }

    @Test
    @DisplayName("실패: Brand가 null이면 Exception이 발생한다")
    void createProduct_WithNullBrand_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> Product.create(validPrice, validCategory, null))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_BRAND_NULL);
    }
}
