package com.dd3ok.musinsatest.domain.product;

import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    @DisplayName("성공: 유효한 값으로 Price 객체를 생성한다")
    void createPrice_Success() {
        // given
        BigDecimal validValue = new BigDecimal("10000");

        // when
        Price price = new Price(validValue);

        // then
        assertThat(price.value()).isEqualByComparingTo(validValue);
    }

    @Test
    @DisplayName("실패: 가격으로 음수를 입력하면 exception 발생한다")
    void createPrice_WithNegativeValue_ThrowsException() {
        // given
        BigDecimal negativeValue = new BigDecimal("-100");

        // when & then
        assertThatThrownBy(() -> new Price(negativeValue))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PRICE_NEGATIVE);
    }

    @Test
    @DisplayName("실패: 가격으로 null을 입력하면 exception 발생한다")
    void createPrice_WithNull_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PRICE_NULL);
    }

    @Test
    @DisplayName("성공: 두 Price 객체를 더한다")
    void addPrice_Success() {
        // given
        Price price1 = new Price(new BigDecimal("1000"));
        Price price2 = new Price(new BigDecimal("2000"));
        BigDecimal expectedValue = new BigDecimal("3000");

        // when
        Price result = price1.add(price2);

        // then
        assertThat(result.value()).isEqualByComparingTo(expectedValue);
    }

    @Test
    @DisplayName("실패: add 메서드에 null을 입력하면 exception 발생한다")
    void addPrice_WithNull_ThrowsException() {
        // given
        Price price = new Price(new BigDecimal("1000"));

        // when & then
        assertThatThrownBy(() -> price.add(null))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PRICE_NULL);
    }

    @Test
    @DisplayName("성공: 가격에 0을 더한다")
    void addPrice_WithZero_Success() {
        // given
        Price price = new Price(new BigDecimal("1000"));
        Price zero = new Price(BigDecimal.ZERO);

        // when
        Price result = price.add(zero);

        // then
        assertThat(result.value()).isEqualByComparingTo(new BigDecimal("1000"));
    }
}