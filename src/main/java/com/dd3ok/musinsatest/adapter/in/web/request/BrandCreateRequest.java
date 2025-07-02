package com.dd3ok.musinsatest.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
@AllArgsConstructor
public class BrandCreateRequest {
    @NotBlank(message = "브랜드 이름은 비어 있을 수 없습니다.")
    private String name;
}