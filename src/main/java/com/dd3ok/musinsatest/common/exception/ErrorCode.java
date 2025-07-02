package com.dd3ok.musinsatest.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    DATA_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),

    // brand
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "브랜드를 찾을 수 없습니다."),
    BRAND_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 브랜드입니다."),
    BRAND_IN_USE(HttpStatus.CONFLICT, "해당 브랜드에 속한 상품이 존재하여 삭제할 수 없습니다."),
    INVALID_BRAND_NULL(HttpStatus.BAD_REQUEST, "브랜드는 null일 수 없습니다."),

    // product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다"),
    INVALID_CATEGORY_NULL(HttpStatus.BAD_REQUEST, "카테고리는 null일 수 없습니다."),

    // price
    INVALID_PRICE_NULL(HttpStatus.BAD_REQUEST, "가격은 null일 수 없습니다."),
    INVALID_PRICE_NEGATIVE(HttpStatus.BAD_REQUEST, "가격은 0보다 작을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
