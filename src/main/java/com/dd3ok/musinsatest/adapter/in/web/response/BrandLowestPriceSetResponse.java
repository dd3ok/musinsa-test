package com.dd3ok.musinsatest.adapter.in.web.response;

import com.dd3ok.musinsatest.application.port.in.dto.BrandLowestPriceSetResult;
import com.dd3ok.musinsatest.common.util.FormatUtil;

import java.util.List;

public record BrandLowestPriceSetResponse(
        LowestPriceSet lowestPriceSet
) {
    public static BrandLowestPriceSetResponse from(BrandLowestPriceSetResult serviceResponse) {
        List<ProductInfo> productInfos = serviceResponse.products().stream()
                .map(p -> new ProductInfo(
                        p.category().getDescription(),
                        FormatUtil.formatPrice(p.price()))
                )
                .toList();

        LowestPriceSet lowestPriceSet = new LowestPriceSet(
                serviceResponse.brandName(),
                productInfos,
                FormatUtil.formatPrice(serviceResponse.totalPrice())
        );

        return new BrandLowestPriceSetResponse(lowestPriceSet);
    }

    public record LowestPriceSet(
            String brand,
            List<ProductInfo> productInfos,
            String totalPrice
    ) {}

    public record ProductInfo(
            String category,
            String price
    ) {}
}