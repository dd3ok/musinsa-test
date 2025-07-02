package com.dd3ok.musinsatest.adapter.in.web.response;

import com.dd3ok.musinsatest.application.port.in.dto.LowestPriceProductsResult;
import com.dd3ok.musinsatest.common.util.FormatUtil;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CategoryLowestPriceResponse {

    private final List<ProductInfo> products;
    private final String totalPrice;

    public CategoryLowestPriceResponse(List<ProductInfo> products, BigDecimal totalPrice) {
        this.products = products;
        this.totalPrice = String.format("%,d", totalPrice.intValue());
    }

    public static CategoryLowestPriceResponse from(LowestPriceProductsResult serviceResponse) {
        List<ProductInfo> productInfos = serviceResponse.products().stream()
                .map(p -> new ProductInfo(
                        p.category().getDescription(),
                        p.brandName(),
                        FormatUtil.formatPrice(p.price())
                ))
                .toList();

        return new CategoryLowestPriceResponse(productInfos, serviceResponse.totalPrice());
    }

    public record ProductInfo(String category, String brandName, String price) { }
}
