package com.dd3ok.musinsatest.domain.brand;

import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Brand {

    private Long id;
    private String name;

    private Brand(Long id, String name) {
        if (name == null || name.isBlank()) {
            throw new BaseException(ErrorCode.INVALID_BRAND_NULL);
        }
        this.id = id;
        this.name = name;
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
