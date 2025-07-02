package com.dd3ok.musinsatest.application.port.in.dto;

import java.math.BigDecimal;
import java.util.List;

public record BrandLowestPriceSetResult(
    String brandName,
    List<CategoryPriceDto> products,
    BigDecimal totalPrice
) {}
