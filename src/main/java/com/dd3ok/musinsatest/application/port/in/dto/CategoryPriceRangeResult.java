package com.dd3ok.musinsatest.application.port.in.dto;

import java.util.List;

public record CategoryPriceRangeResult(
    String category,
    List<BrandPriceDto> lowestPriceProducts,
    List<BrandPriceDto> highestPriceProducts
) {}