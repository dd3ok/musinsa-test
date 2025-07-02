package com.dd3ok.musinsatest.application.port.in.command;

public record ProductUpdateCommand(
        Long productId,
        Long brandId,
        String category,
        Integer price
) {
}