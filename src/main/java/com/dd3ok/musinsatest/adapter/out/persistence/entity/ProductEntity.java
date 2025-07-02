package com.dd3ok.musinsatest.adapter.out.persistence.entity;

import com.dd3ok.musinsatest.domain.product.Category;
import com.dd3ok.musinsatest.domain.product.Price;
import com.dd3ok.musinsatest.domain.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    protected ProductEntity(BigDecimal price, Category category, BrandEntity brand) {
        this.price = price;
        this.category = category;
        this.brand = brand;
    }

    protected ProductEntity(Long id, BigDecimal price, Category category, BrandEntity brand) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.brand = brand;
    }

    public static ProductEntity from(Product product, BrandEntity brandEntity) {
        if (product.getId() != null) {
            return new ProductEntity(
                    product.getId(),
                    product.getPrice().value(),
                    product.getCategory(),
                    brandEntity
            );
        } else {
            return new ProductEntity(
                    product.getPrice().value(),
                    product.getCategory(),
                    brandEntity
            );
        }
    }

    public Product toDomain() {
        return Product.from(
                this.id,
                new Price(this.price),
                this.category,
                this.brand.toDomain()
        );
    }

    public void update(BigDecimal price, Category category, BrandEntity brand) {
        this.price = price;
        this.category = category;
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
