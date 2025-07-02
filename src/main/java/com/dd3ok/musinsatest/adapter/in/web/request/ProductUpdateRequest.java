package com.dd3ok.musinsatest.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {
    private Long brandId;
    private String category;
    private Integer price;
}