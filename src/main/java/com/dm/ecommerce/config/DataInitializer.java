package com.dm.ecommerce.config;

import com.dm.ecommerce.model.Category;
import com.dm.ecommerce.model.Product;
import com.dm.ecommerce.model.User;
import com.dm.ecommerce.repository.CategoryRepository;
import com.dm.ecommerce.repository.ProductRepository;
import com.dm.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

/**
 * Initializes the database with some sample data.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create users
        createDefaultUsers();
        
        // Create categories and products
        createDefaultCategoriesAndProducts();
    }
    
    private void createDefaultUsers() {
        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            
            // Initialize collections
            adminUser.setRoles(new HashSet<>());
            
            // Add ADMIN role
            adminUser.getRoles().add(User.Role.ADMIN);
            
            userRepository.save(adminUser);
            System.out.println("Admin user created successfully");
        }
        
        // Create regular user if it doesn't exist
        if (!userRepository.existsByUsername("user")) {
            User regularUser = new User();
            regularUser.setUsername("user");
            regularUser.setEmail("user@example.com");
            regularUser.setPassword(passwordEncoder.encode("user123"));
            
            // Initialize collections
            regularUser.setRoles(new HashSet<>());
            
            // Add USER role
            regularUser.getRoles().add(User.Role.USER);
            
            userRepository.save(regularUser);
            System.out.println("Regular user created successfully");
        }
    }
    
    private void createDefaultCategoriesAndProducts() {
        // Check if products already exist
        if (productRepository.count() > 0) {
            System.out.println("Products already exist, skipping creation");
            return;
        }
        
        // Create Electronics category
        Category electronics = createCategoryIfNotExists("Electronics", "Electronic devices and gadgets");
        
        // Create Clothing category
        Category clothing = createCategoryIfNotExists("Clothing", "Apparel and fashion items");
        
        // Create Books category
        Category books = createCategoryIfNotExists("Books", "Books and literature");
        
        // Create Home & Kitchen category
        Category homeKitchen = createCategoryIfNotExists("Home & Kitchen", "Home appliances and kitchen essentials");
        
        // Add Electronics products
        createProduct("Smartphone XS", "Latest smartphone with advanced features", new BigDecimal("699.99"), 50, electronics);
        createProduct("Laptop Pro", "High-performance laptop for professionals", new BigDecimal("1299.99"), 30, electronics);
        createProduct("Wireless Earbuds", "Premium sound quality with noise cancellation", new BigDecimal("149.99"), 100, electronics);
        
        // Add Clothing products
        createProduct("Classic T-Shirt", "Comfortable cotton t-shirt", new BigDecimal("19.99"), 200, clothing);
        createProduct("Denim Jeans", "Stylish and durable jeans", new BigDecimal("49.99"), 150, clothing);
        
        // Add Books products
        createProduct("Java Programming", "Comprehensive guide to Java programming", new BigDecimal("39.99"), 80, books);
        createProduct("Spring Boot in Action", "Learn Spring Boot framework", new BigDecimal("44.99"), 60, books);
        
        // Add Home & Kitchen products
        createProduct("Coffee Maker", "Programmable coffee maker with timer", new BigDecimal("79.99"), 40, homeKitchen);
        createProduct("Blender", "High-speed blender for smoothies", new BigDecimal("59.99"), 50, homeKitchen);
        createProduct("Toaster", "4-slice toaster with multiple settings", new BigDecimal("34.99"), 70, homeKitchen);
        
        System.out.println("Default categories and products created successfully");
    }
    
    private Category createCategoryIfNotExists(String name, String description) {
        if (!categoryRepository.existsByName(name)) {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            return categoryRepository.save(category);
        }
        return categoryRepository.findByName(name);
    }
    
    private Product createProduct(String name, String description, BigDecimal price, Integer stockQuantity, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setCategory(category);
        return productRepository.save(product);
    }
} 