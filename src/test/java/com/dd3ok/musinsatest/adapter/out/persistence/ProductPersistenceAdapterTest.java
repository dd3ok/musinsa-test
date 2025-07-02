package com.dd3ok.musinsatest.adapter.out.persistence;

import com.dd3ok.musinsatest.adapter.out.persistence.mapper.BrandMapper;
import com.dd3ok.musinsatest.adapter.out.persistence.mapper.ProductMapper;
import com.dd3ok.musinsatest.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ProductPersistenceAdapter.class, ProductMapper.class, BrandMapper.class}) // 테스트에 필요한 Bean들을 직접 임포트
class ProductPersistenceAdapterTest {

    @Autowired
    private ProductPersistenceAdapter productPersistenceAdapter;

    @Test
    @DisplayName("카테고리별 최저가 상품 조회가 성공한다")
    void 카테고리별_최저가_상품조회_성공() {
        // when
        List<Product> result = productPersistenceAdapter.findLowestPriceProductsGroupByCategory();

        // then
        // 8개 카테고리지만 같은 가격 상품도 조회
        assertThat(result).hasSize(9);

        // '상의' 카테고리의 최저가 상품은 브랜드 'C', 가격 10,000원임을 검증
        Product topProduct = result.stream()
                .filter(p -> p.category().name().equals("TOP"))
                .findFirst()
                .orElse(null);

        assertThat(topProduct).isNotNull();
        assertThat(topProduct.brand().name()).isEqualTo("C");
        assertThat(topProduct.price().value()).isEqualByComparingTo("10000");
    }
}
