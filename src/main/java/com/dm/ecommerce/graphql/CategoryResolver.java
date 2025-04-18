package com.dm.ecommerce.graphql;

import com.dm.ecommerce.model.Category;
import com.dm.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL resolver for category-related operations.
 */
@Controller
public class CategoryResolver {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get a category by ID
     *
     * @param id the category ID
     * @return the category if found
     */
    @QueryMapping
    public Category getCategoryById(@Argument String id) {
        try {
            Long categoryId = Long.parseLong(id);
            return categoryRepository.findById(categoryId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Get all categories
     *
     * @return list of all categories
     */
    @QueryMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
} 