package com.dd3ok.musinsatest.domain.product;

import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    TOP("상의"),
    OUTER("아우터"),
    PANTS("바지"),
    SNEAKERS("스니커즈"),
    BAG("가방"),
    HAT("모자"),
    SOCKS("양말"),
    ACCESSORIES("액세서리");

    private final String description;

    public static Category fromString(String name) {
        try {
            return Category.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BaseException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }
}