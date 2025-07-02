package com.dd3ok.musinsatest.application.port.in.dto;

import com.dd3ok.musinsatest.domain.product.Category;

import java.math.BigDecimal;

public record CategoryPriceDto(Category category, BigDecimal price) {}