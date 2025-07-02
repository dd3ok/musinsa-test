package com.dd3ok.musinsatest.adapter.in.web;

import com.dd3ok.musinsatest.adapter.in.web.request.BrandCreateRequest;
import com.dd3ok.musinsatest.adapter.in.web.request.BrandUpdateRequest;
import com.dd3ok.musinsatest.application.port.in.BrandCommandUseCase;
import com.dd3ok.musinsatest.application.port.in.command.BrandCreateCommand;
import com.dd3ok.musinsatest.application.port.in.command.BrandUpdateCommand;
import com.dd3ok.musinsatest.domain.brand.Brand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandCommandController {

    private final BrandCommandUseCase brandCommandUseCase;

    @PostMapping
    public ResponseEntity<Void> createBrand(@Valid @RequestBody BrandCreateRequest request) {
        BrandCreateCommand command = new BrandCreateCommand(request.getName());
        Brand createdBrand = brandCommandUseCase.createBrand(command);
        URI location = URI.create("/api/v1/admin/brands/" + createdBrand.id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<Void> updateBrand(@PathVariable Long brandId, @RequestBody BrandUpdateRequest request) {
        BrandUpdateCommand command = new BrandUpdateCommand(brandId, request.getName());
        brandCommandUseCase.updateBrand(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long brandId) {
        brandCommandUseCase.deleteBrand(brandId);
        return ResponseEntity.noContent().build();
    }

    // COMMENT: 요구사항x 4번 테스트 후 조회를 위해 조회 추가
    @GetMapping
    public ResponseEntity<List<Brand>> findAllBrand() {
        List<Brand> allBrands = brandCommandUseCase.getAllBrands();
        return ResponseEntity.ok(allBrands);
    }
}
