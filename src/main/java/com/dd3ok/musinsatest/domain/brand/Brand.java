package com.dd3ok.musinsatest.domain.brand;

import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;

public record Brand(Long id, String name) {

    public Brand {
        if (name == null || name.isBlank()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    public static Brand create(String name) {
        return new Brand(null, name);
    }

    public static Brand from(Long id, String name) {
        return new Brand(id, name);
    }

    public Brand updateName(String newName) {
        return new Brand(this.id, newName);
    }
}