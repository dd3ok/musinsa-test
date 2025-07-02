package com.dd3ok.musinsatest.application.port.in.command;

public record BrandUpdateCommand(
    Long brandId,
    String brandName
) {
}