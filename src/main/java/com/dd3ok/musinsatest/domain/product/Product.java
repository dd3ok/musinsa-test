package com.dd3ok.musinsatest.domain.product;

import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
import com.dd3ok.musinsatest.domain.brand.Brand;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Product {

    private Long id;
    private Price price;
    private Category category;
    private Brand brand;

    private Product(Long id, Price price, Category category, Brand brand) {
        if (price == null) {
            throw new BaseException(ErrorCode.INVALID_PRICE_NULL);
        }
        if (category == null) {
            throw new BaseException(ErrorCode.INVALID_CATEGORY_NULL);
        }
        if (brand == null) {
            throw new BaseException(ErrorCode.INVALID_BRAND_NULL);
        }
        this.id = id;
        this.price = price;
        this.category = category;
        this.brand = brand;
    }

    public static Product create(Price price, Category category, Brand brand) {
        return new Product(null, price, category, brand);
    }

    public static Product from(Long id, Price price, Category category, Brand brand) {
        return new Product(id, price, category, brand);
    }

    public Product update(Price newPrice, Category newCategory, Brand newBrand) {
        return new Product(
                this.id,
                newPrice != null ? newPrice : this.price,
                newCategory != null ? newCategory : this.category,
                newBrand != null ? newBrand : this.brand
        );
    }
}
