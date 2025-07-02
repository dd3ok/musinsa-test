package com.dd3ok.musinsatest.application.port.in;

import com.dd3ok.musinsatest.application.port.in.dto.CategoryPriceRangeResult;

public interface CategoryLowHighPriceUseCase {
    CategoryPriceRangeResult getCategoryLowAndHighPrice(String categoryName);
}
