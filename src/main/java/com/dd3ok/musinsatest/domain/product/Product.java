package com.dd3ok.musinsatest.domain.product;

import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    private Long id;
    private Price price;
    private Category category;
    private Brand brand;

    private Product(Price price, Category category, Brand brand) {
        if (price == null) {
            throw new BaseException(ErrorCode.INVALID_PRICE_NULL);
        } else if (category == null) {
            throw new BaseException(ErrorCode.INVALID_CATEGORY_NULL);
        } else if (brand == null) {
            throw new BaseException(ErrorCode.INVALID_BRAND_NULL);
        }

        this.price = price;
        this.category = category;
        this.brand = brand;
    }

    public static Product create(Price price, Category category, Brand brand) {
        return new Product(price, category, brand);
    }

    public void update(Price newPrice, Category newCategory, Brand newBrand) {
        // 수정 로직을 한 곳에서 관리
        if (newPrice != null) {
            this.price = newPrice;
        }
        if (newCategory != null) {
            this.category = newCategory;
        }
        if (newBrand != null) {
            this.brand = newBrand;
        }
    }

    // Mapper나 테스트를 위한 생성자
    public Product(Long id, Price price, Category category, Brand brand) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.brand = brand;
    }
}