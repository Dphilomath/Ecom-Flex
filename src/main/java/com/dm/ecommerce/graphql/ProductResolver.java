package com.dm.ecommerce.graphql;

import com.dm.ecommerce.model.Category;
import com.dm.ecommerce.model.Product;
import com.dm.ecommerce.repository.CategoryRepository;
import com.dm.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

/**
 * GraphQL resolver for product-related operations.
 */
@Controller
public class ProductResolver {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get a product by ID
     *
     * @param id the product ID
     * @return the product if found
     */
    @QueryMapping
    public Product getProductById(@Argument String id) {
        try {
            Long productId = Long.parseLong(id);
            return productRepository.findById(productId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Get all products
     *
     * @return list of all products
     */
    @QueryMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get products by category
     *
     * @param categoryId the category ID
     * @return list of products in the category
     */
    @QueryMapping
    public List<Product> getProductsByCategory(@Argument String categoryId) {
        try {
            Long catId = Long.parseLong(categoryId);
            return productRepository.findByCategoryId(catId);
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    /**
     * Search products by keyword
     *
     * @param keyword the search keyword
     * @return list of matching products
     */
    @QueryMapping
    public List<Product> searchProducts(@Argument String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
} 