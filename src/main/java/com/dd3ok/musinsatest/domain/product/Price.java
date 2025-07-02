package com.dd3ok.musinsatest.domain.product;

import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;

import java.math.BigDecimal;

public record Price(BigDecimal value) {
    public Price {
        if (value == null) {
            throw new BaseException(ErrorCode.INVALID_PRICE_NULL);
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BaseException(ErrorCode.INVALID_PRICE_NEGATIVE);
        }
    }

    public Price add(Price other) {
        if (other == null) {
            throw new BaseException(ErrorCode.INVALID_PRICE_NULL);
        }
        return new Price(this.value.add(other.value()));
    }
}