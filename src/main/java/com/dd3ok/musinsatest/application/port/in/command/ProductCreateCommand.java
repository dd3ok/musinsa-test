package com.dd3ok.musinsatest.application.port.in.command;

public record ProductCreateCommand(
    Long brandId,
    String category,
    int price
) {
}