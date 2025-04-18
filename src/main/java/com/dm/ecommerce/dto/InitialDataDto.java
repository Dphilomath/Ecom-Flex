package com.dm.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InitialDataDto {
    private List<CategoryDto> categories;
    private List<ProductDto> products;

    @Data
    public static class CategoryDto {
        private String name;
        private String description;
    }

    @Data
    public static class ProductDto {
        private String name;
        private String description;
        private String categoryName;
        private BigDecimal price;
        private Integer stockQuantity;
    }
} 