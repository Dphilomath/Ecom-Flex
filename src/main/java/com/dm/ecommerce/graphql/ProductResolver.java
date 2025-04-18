package com.dm.ecommerce.graphql;

import com.dm.ecommerce.model.Category;
import com.dm.ecommerce.model.Product;
import com.dm.ecommerce.model.Review;
import com.dm.ecommerce.repository.CategoryRepository;
import com.dm.ecommerce.repository.ProductRepository;
import com.dm.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GraphQL resolver for product-related operations.
 */
@Controller
public class ProductResolver {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;

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
    
    /**
     * Get product with detailed information including category and reviews
     *
     * @param id the product ID
     * @return ProductWithDetails object containing product, category, and reviews
     */
    @QueryMapping
    public Map<String, Object> getProductWithDetails(@Argument String id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Long productId = Long.parseLong(id);
            Optional<Product> optionalProduct = productRepository.findById(productId);
            
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                List<Review> reviews = product.getReviews();
                
                // Calculate average rating
                double averageRating = 0.0;
                if (!reviews.isEmpty()) {
                    averageRating = reviews.stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0.0);
                }
                
                result.put("product", product);
                result.put("category", product.getCategory());
                result.put("reviews", reviews);
                result.put("averageRating", BigDecimal.valueOf(averageRating)
                        .setScale(1, RoundingMode.HALF_UP).doubleValue());
                result.put("inStock", product.getStockQuantity() > 0);
            }
        } catch (NumberFormatException e) {
            // Return empty result on error
        }
        
        return result;
    }
    
    /**
     * Get top rated products
     *
     * @param limit the maximum number of products to return
     * @return list of top rated products
     */
    @QueryMapping
    public List<Product> getTopRatedProducts(@Argument Integer limit) {
        int resultsLimit = (limit != null && limit > 0) ? limit : 10;
        
        // This is a simplified implementation - in a real system with a larger
        // database, you would use SQL queries with JOINs and GROUP BY for better performance
        List<Product> allProducts = productRepository.findAll();
        
        return allProducts.stream()
                .filter(product -> !product.getReviews().isEmpty())
                .sorted((p1, p2) -> {
                    double avg1 = p1.getReviews().stream().mapToInt(Review::getRating).average().orElse(0);
                    double avg2 = p2.getReviews().stream().mapToInt(Review::getRating).average().orElse(0);
                    return Double.compare(avg2, avg1); // Sort descending
                })
                .limit(resultsLimit)
                .collect(Collectors.toList());
    }
    
    /**
     * Get product review summary
     *
     * @param productId the product ID
     * @return ReviewSummary object with statistics
     */
    @QueryMapping
    public Map<String, Object> getProductReviewSummary(@Argument String productId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Long id = Long.parseLong(productId);
            Optional<Product> optionalProduct = productRepository.findById(id);
            
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                List<Review> reviews = product.getReviews();
                
                int totalReviews = reviews.size();
                double averageRating = reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
                
                // Calculate rating distribution
                Map<Integer, Long> ratingCounts = reviews.stream()
                        .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));
                
                List<Map<String, Object>> ratingDistribution = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    Map<String, Object> ratingCount = new HashMap<>();
                    ratingCount.put("rating", i);
                    ratingCount.put("count", ratingCounts.getOrDefault(i, 0L).intValue());
                    ratingDistribution.add(ratingCount);
                }
                
                result.put("totalReviews", totalReviews);
                result.put("averageRating", BigDecimal.valueOf(averageRating)
                        .setScale(1, RoundingMode.HALF_UP).doubleValue());
                result.put("ratingDistribution", ratingDistribution);
            }
        } catch (NumberFormatException e) {
            // Return empty result on error
        }
        
        return result;
    }
    
    /**
     * Get products by category with additional details
     *
     * @param categoryId the category ID
     * @return ProductsByCategory object with category, products, and count
     */
    @QueryMapping
    public Map<String, Object> getProductsByCategoryWithDetails(@Argument String categoryId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Long catId = Long.parseLong(categoryId);
            Optional<Category> optionalCategory = categoryRepository.findById(catId);
            
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                List<Product> products = productRepository.findByCategoryId(catId);
                
                result.put("category", category);
                result.put("products", products);
                result.put("totalCount", products.size());
            }
        } catch (NumberFormatException e) {
            // Return empty result on error
        }
        
        return result;
    }
    
    /**
     * Get related products based on the same category
     *
     * @param productId the product ID
     * @param limit the maximum number of related products to return
     * @return list of related products
     */
    @QueryMapping
    public List<Product> getRelatedProducts(@Argument String productId, @Argument Integer limit) {
        int resultsLimit = (limit != null && limit > 0) ? limit : 5;
        
        try {
            Long id = Long.parseLong(productId);
            Optional<Product> optionalProduct = productRepository.findById(id);
            
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                Category category = product.getCategory();
                
                if (category != null) {
                    // Get products in the same category excluding the current product
                    return productRepository.findByCategoryId(category.getId()).stream()
                            .filter(p -> !p.getId().equals(product.getId()))
                            .limit(resultsLimit)
                            .collect(Collectors.toList());
                }
            }
        } catch (NumberFormatException e) {
            // Return empty list on error
        }
        
        return List.of();
    }
    
    /**
     * Get filtered and sorted products with pagination
     *
     * @param filter filter criteria
     * @param sort sort options
     * @param page page number (0-based)
     * @param size page size
     * @return filtered list of products
     */
    @QueryMapping
    public List<Product> getFilteredProducts(
            @Argument Map<String, Object> filter,
            @Argument Map<String, String> sort,
            @Argument Integer page,
            @Argument Integer size) {
        
        // Default pagination values
        int pageNum = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0) ? size : 10;
        
        // Get all products and apply filtering, sorting, and pagination in memory
        // In a real application, this would be implemented with a more efficient database query
        List<Product> allProducts = productRepository.findAll();
        
        // Apply filters
        Stream<Product> filteredProducts = allProducts.stream();
        
        if (filter != null) {
            // Price range filter
            if (filter.containsKey("minPrice")) {
                Double minPrice = (Double) filter.get("minPrice");
                filteredProducts = filteredProducts.filter(p -> 
                        p.getPrice().doubleValue() >= minPrice);
            }
            
            if (filter.containsKey("maxPrice")) {
                Double maxPrice = (Double) filter.get("maxPrice");
                filteredProducts = filteredProducts.filter(p -> 
                        p.getPrice().doubleValue() <= maxPrice);
            }
            
            // Category filter
            if (filter.containsKey("categoryIds")) {
                List<String> categoryIds = (List<String>) filter.get("categoryIds");
                List<Long> catIds = categoryIds.stream()
                        .map(id -> {
                            try {
                                return Long.parseLong(id);
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                
                filteredProducts = filteredProducts.filter(p -> 
                        p.getCategory() != null && catIds.contains(p.getCategory().getId()));
            }
            
            // Stock filter
            if (filter.containsKey("inStock")) {
                Boolean inStock = (Boolean) filter.get("inStock");
                filteredProducts = filteredProducts.filter(p -> 
                        inStock ? p.getStockQuantity() > 0 : p.getStockQuantity() == 0);
            }
            
            // Rating filter
            if (filter.containsKey("minRating")) {
                Integer minRating = (Integer) filter.get("minRating");
                filteredProducts = filteredProducts.filter(p -> {
                    if (p.getReviews().isEmpty()) {
                        return false;
                    }
                    double avgRating = p.getReviews().stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0.0);
                    return avgRating >= minRating;
                });
            }
        }
        
        // Apply sorting
        if (sort != null && sort.containsKey("field") && sort.containsKey("direction")) {
            String field = sort.get("field");
            String direction = sort.get("direction");
            boolean ascending = "ASC".equalsIgnoreCase(direction);
            
            Comparator<Product> comparator = null;
            
            switch (field.toLowerCase()) {
                case "price":
                    comparator = Comparator.comparing(p -> p.getPrice().doubleValue());
                    break;
                case "name":
                    comparator = Comparator.comparing(Product::getName);
                    break;
                case "rating":
                    comparator = Comparator.comparing(p -> {
                        if (p.getReviews().isEmpty()) {
                            return 0.0;
                        }
                        return p.getReviews().stream()
                                .mapToInt(Review::getRating)
                                .average()
                                .orElse(0.0);
                    });
                    break;
                default:
                    // Default to sorting by ID
                    comparator = Comparator.comparing(Product::getId);
            }
            
            if (!ascending) {
                comparator = comparator.reversed();
            }
            
            filteredProducts = filteredProducts.sorted(comparator);
        }
        
        // Apply pagination
        return filteredProducts
                .skip((long) pageNum * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }
    
    /**
     * Get product summaries by IDs
     *
     * @param ids list of product IDs
     * @return list of ProductSummary objects
     */
    @QueryMapping
    public List<Map<String, Object>> getProductSummaries(@Argument List<String> ids) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        List<Long> productIds = ids.stream()
                .map(id -> {
                    try {
                        return Long.parseLong(id);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        List<Product> products = productRepository.findAllById(productIds);
        
        for (Product product : products) {
            Map<String, Object> summary = new HashMap<>();
            
            summary.put("id", product.getId().toString());
            summary.put("name", product.getName());
            summary.put("price", product.getPrice().doubleValue());
            
            if (product.getCategory() != null) {
                summary.put("categoryName", product.getCategory().getName());
            }
            
            // Calculate average rating
            if (!product.getReviews().isEmpty()) {
                double averageRating = product.getReviews().stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
                
                summary.put("averageRating", BigDecimal.valueOf(averageRating)
                        .setScale(1, RoundingMode.HALF_UP).doubleValue());
            }
            
            results.add(summary);
        }
        
        return results;
    }

    /**
     * Get products that are on sale (simulated)
     * In a real system, you would have a "salePrice" or "onSale" field on the Product entity
     *
     * @return list of products on sale
     */
    @QueryMapping
    public List<Product> getProductsOnSale() {
        // Simulate sale products by returning a subset of products
        // In a real application, this would query products with a sale flag or discount field
        List<Product> allProducts = productRepository.findAll();
        
        // For this example, we'll just return 20% of the products, selected randomly
        int saleProductCount = Math.max(1, allProducts.size() / 5);
        
        return allProducts.stream()
                .sorted((p1, p2) -> (int) (Math.random() * 3) - 1) // Random shuffle
                .limit(saleProductCount)
                .collect(Collectors.toList());
    }
} 