package com.dd3ok.musinsatest.domain.product;

import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;

public record Product(Long id, Price price, Category category, Brand brand) {

    public Product {
        if (price == null) {
            throw new BaseException(ErrorCode.INVALID_PRICE_NULL);
        }
        if (category == null) {
            throw new BaseException(ErrorCode.INVALID_CATEGORY_NULL);
        }
        if (brand == null) {
            throw new BaseException(ErrorCode.INVALID_BRAND_NULL);
        }
    }

    public static Product create(Price price, Category category, Brand brand) {
        return new Product(null, price, category, brand);
    }

    public static Product from(Long id, Price price, Category category, Brand brand) {
        return new Product(id, price, category, brand);
    }

    public Product updatePrice(Price newPrice) {
        return new Product(this.id, newPrice, this.category, this.brand);
    }

    public Product updateCategory(Category newCategory) {
        return new Product(this.id, this.price, newCategory, this.brand);
    }

    public Product updateBrand(Brand newBrand) {
        return new Product(this.id, this.price, this.category, newBrand);
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
