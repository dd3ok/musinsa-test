package com.dd3ok.musinsatest.application.service;

import com.dd3ok.musinsatest.application.port.in.BrandCommandUseCase;
import com.dd3ok.musinsatest.application.port.in.command.BrandCreateCommand;
import com.dd3ok.musinsatest.application.port.in.command.BrandUpdateCommand;
import com.dd3ok.musinsatest.application.port.out.BrandRepository;
import com.dd3ok.musinsatest.application.port.out.ProductRepository;
import com.dd3ok.musinsatest.domain.brand.Brand;
import com.dd3ok.musinsatest.common.exception.BaseException;
import com.dd3ok.musinsatest.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandCommandService implements BrandCommandUseCase {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    public Brand createBrand(BrandCreateCommand command) {
        brandRepository.findByName(command.brandName()).ifPresent(b -> {
            throw new BaseException(ErrorCode.BRAND_NAME_DUPLICATED);
        });

        Brand newBrand = Brand.create(command.brandName());

        return brandRepository.save(newBrand);
    }

    @Override
    public Brand updateBrand(BrandUpdateCommand command) {
        brandRepository.findByName(command.brandName()).ifPresent(b -> {
            if (!b.getId().equals(command.brandId())) {
                throw new BaseException(ErrorCode.BRAND_NAME_DUPLICATED);
            }
        });

        Brand brand = brandRepository.findById(command.brandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        Brand updatedBrand = brand.updateName(command.brandName());

        return brandRepository.save(updatedBrand);
    }

    @Override
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_NOT_FOUND));

        if (productRepository.existsByBrandId(brandId)) {
            throw new BaseException(ErrorCode.BRAND_IN_USE);
        }

        brandRepository.delete(brand);
    }

    // COMMENT: command지만 4번 API 조회 테스트를 위해 조회 추가합니다.
    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
