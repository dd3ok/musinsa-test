package com.dd3ok.musinsatest.application.port.in;

import com.dd3ok.musinsatest.application.port.in.dto.LowestPriceProductsResult;

public interface LowestPriceProductsUseCase {
    LowestPriceProductsResult getLowestPriceProductsByCategory();
}