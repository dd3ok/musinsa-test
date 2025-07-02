package com.dd3ok.musinsatest.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    private Long brandId;
    @NotNull(message = "상품 카테고리는 필수 입력값입니다.")
    private String category;
    @NotNull(message = "상품 가격은 필수 입력값입니다.")
    private Integer price;
}
