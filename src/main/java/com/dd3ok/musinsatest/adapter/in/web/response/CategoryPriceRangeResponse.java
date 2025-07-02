package com.dd3ok.musinsatest.adapter.in.web.response;

import com.dd3ok.musinsatest.application.port.in.dto.CategoryPriceRangeResult;
import com.dd3ok.musinsatest.global.util.FormatUtil;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryPriceRangeResponse {
    private final String category;
    private final List<BrandPriceInfo> lowestPrices;
    private final List<BrandPriceInfo> highestPrices;

    public CategoryPriceRangeResponse(String category, List<BrandPriceInfo> lowest, List<BrandPriceInfo> highest) {
        this.category = category;
        this.lowestPrices = lowest;
        this.highestPrices = highest;
    }
    
    public static CategoryPriceRangeResponse from(CategoryPriceRangeResult serviceResponse) {
        List<BrandPriceInfo> lowest = serviceResponse.lowestPriceProducts().stream()
            .map(p -> new BrandPriceInfo(p.brandName(), FormatUtil.formatPrice(p.price())))
            .toList();
        
        List<BrandPriceInfo> highest = serviceResponse.highestPriceProducts().stream()
            .map(p -> new BrandPriceInfo(p.brandName(), FormatUtil.formatPrice(p.price())))
            .toList();
            
        return new CategoryPriceRangeResponse(serviceResponse.category(), lowest, highest);
    }

    public record BrandPriceInfo(String brand, String price) { }
}