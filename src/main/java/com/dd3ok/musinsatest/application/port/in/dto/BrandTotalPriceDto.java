package com.dd3ok.musinsatest.application.port.in.dto;

import java.math.BigDecimal;

public record BrandTotalPriceDto(
    Long brandId,
    BigDecimal totalPrice
) {}