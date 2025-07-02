package com.dd3ok.musinsatest;

import com.dd3ok.musinsatest.adapter.in.web.request.ProductCreateRequest;
import com.dd3ok.musinsatest.adapter.in.web.request.ProductUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("API 1: 카테고리별 최저가 상품 조회를 요청하면 성공적으로 응답한다")
    void getLowestPriceProductsByCategory_Success() throws Exception {
        mockMvc.perform(get("/api/v1/products/lowest-price/group-category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.totalPrice").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("API 2: 단일 브랜드 최저가 세트 조회를 요청하면 성공적으로 응답한다")
    void getLowestPriceBrandSet_Success() throws Exception {
        mockMvc.perform(get("/api/v1/products/lowest-price/brand-set")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lowestPriceSet.brand").exists())
                .andExpect(jsonPath("$.lowestPriceSet.productInfos").isArray())
                .andExpect(jsonPath("$.lowestPriceSet.totalPrice").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("API 3: 특정 카테고리의 최저가, 최고가 상품 조회를 요청하면 성공적으로 응답한다")
    void getCategoryLowAndHighPrice_Success() throws Exception {
        String categoryName = "TOP";
        mockMvc.perform(get("/api/v1/products/categories/{categoryName}/low-high-price", categoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("상의"))
                .andExpect(jsonPath("$.lowestPrices").isArray())
                .andExpect(jsonPath("$.highestPrices").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("API 3 실패: 존재하지 않는 카테고리로 조회 요청 시 400 Bad Request를 반환한다")
    void getCategoryLowAndHighPrice_Fail_WithInvalidCategory() throws Exception {
        String invalidCategoryName = "INVALID_CATEGORY";
        mockMvc.perform(get("/api/v1/products/categories/{categoryName}/low-high-price", invalidCategoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @DisplayName("브랜드 생성 API: 새로운 브랜드를 성공적으로 생성하고 201 Created를 반환한다")
    void createBrand_Success() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("name", "NewBrand");

        mockMvc.perform(post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    @DisplayName("브랜드 생성 API 실패: 중복된 이름으로 생성 요청 시 409 Conflict를 반환한다")
    void createBrand_Fail_WithDuplicateName() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("name", "A"); // data.sql에 이미 존재하는 브랜드

        mockMvc.perform(post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("브랜드 수정 API: 브랜드를 성공적으로 수정하고 200 OK를 반환한다")
    void updateBrand_Success() throws Exception {
        Long brandId = 1L; // 'A' 브랜드
        Map<String, String> request = new HashMap<>();
        request.put("name", "A_Updated");

        mockMvc.perform(patch("/api/v1/brands/{brandId}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("브랜드 삭제 API: 브랜드를 성공적으로 삭제하고 204 No Content를 반환한다")
    void deleteBrand_Success() throws Exception {
        // 먼저 삭제할 브랜드를 생성
        Map<String, String> createRequest = new HashMap<>();
        createRequest.put("name", "ToDeleteBrand");
        String response = mockMvc.perform(post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        Long brandId = Long.parseLong(response.substring(response.lastIndexOf('/') + 1));

        // 생성된 브랜드 삭제
        mockMvc.perform(delete("/api/v1/brands/{brandId}", brandId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("브랜드 삭제 API 실패: 상품이 존재하는 브랜드를 삭제 요청 시 409 Conflict를 반환한다")
    void deleteBrand_Fail_WhenProductsExist() throws Exception {
        Long brandIdWithProducts = 1L; // 'A' 브랜드, 상품이 존재함

        mockMvc.perform(delete("/api/v1/brands/{brandId}", brandIdWithProducts))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 생성 API: 새로운 상품을 성공적으로 생성하고 201 Created를 반환한다")
    void createProduct_Success() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(1L, "TOP", 20000);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정 API: 상품 정보를 성공적으로 수정하고 200 OK를 반환한다")
    void updateProduct_Success() throws Exception {
        Long productId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest(2L, "PANTS", 99999);

        mockMvc.perform(patch("/api/v1/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정 API 실패: 존재하지 않는 상품 ID로 요청 시 404 Not Found를 반환한다")
    void updateProduct_Fail_WithInvalidProductId() throws Exception {
        Long invalidProductId = 999L;
        ProductUpdateRequest request = new ProductUpdateRequest(1L, "TOP", 10000);

        mockMvc.perform(patch("/api/v1/products/{productId}", invalidProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 삭제 API: 상품을 성공적으로 삭제하고 204 No Content를 반환한다")
    void deleteProduct_Success() throws Exception {
        Long productId = 1L;
        mockMvc.perform(delete("/api/v1/products/{productId}", productId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 삭제 API 실패: 존재하지 않는 상품 ID로 요청 시 404 Not Found를 반환한다")
    void deleteProduct_Fail_WithInvalidProductId() throws Exception {
        Long invalidProductId = 999L;
        mockMvc.perform(delete("/api/v1/products/{productId}", invalidProductId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}