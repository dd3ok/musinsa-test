package com.dd3ok.musinsatest.adapter.out.persistence.entity;

import com.dd3ok.musinsatest.domain.brand.Brand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "brand")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    protected BrandEntity(String name) {
        this.name = name;
    }

    protected BrandEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static BrandEntity from(Brand brand) {
        if (brand.id() != null) {
            return new BrandEntity(brand.id(), brand.name());
        } else {
            return new BrandEntity(brand.name());
        }
    }

    public Brand toDomain() {
        return Brand.from(this.id, this.name);
    }

    public void updateName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandEntity that = (BrandEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
