package com.dd3ok.musinsatest.application.port.in;

import com.dd3ok.musinsatest.application.port.in.command.BrandCreateCommand;
import com.dd3ok.musinsatest.application.port.in.command.BrandUpdateCommand;
import com.dd3ok.musinsatest.domain.brand.Brand;

import java.util.List;

public interface BrandCommandUseCase {
    Brand createBrand(BrandCreateCommand command);
    Brand updateBrand(BrandUpdateCommand command);
    void deleteBrand(Long brandId);
    List<Brand> getAllBrands();
}