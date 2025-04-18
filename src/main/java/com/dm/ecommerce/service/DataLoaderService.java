package com.dm.ecommerce.service;

import com.dm.ecommerce.dto.InitialDataDto;
import com.dm.ecommerce.model.Category;
import com.dm.ecommerce.model.Product;
import com.dm.ecommerce.model.User;
import com.dm.ecommerce.model.Cart;
import com.dm.ecommerce.repository.CategoryRepository;
import com.dm.ecommerce.repository.ProductRepository;
import com.dm.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataLoaderService implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) {
        if (isDataAlreadyLoaded()) {
            log.info("Data already loaded. Skipping initialization.");
            return;
        }

        try {
            // Create default users
            createDefaultUsers();
            
            // Load and initialize products and categories
            InitialDataDto initialData = loadInitialData();
            initializeData(initialData);
            log.info("Initial data loaded successfully!");
        } catch (IOException e) {
            log.error("Failed to load initial data", e);
        }
    }

    private boolean isDataAlreadyLoaded() {
        return (categoryRepository.count() > 0 && productRepository.count() > 0) 
                || userRepository.count() > 0;
    }

    private void createDefaultUsers() {
        log.info("Creating default users...");

        // Create admin user
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRoles(new HashSet<>());
        adminUser.getRoles().add(User.Role.ADMIN);
        adminUser.getRoles().add(User.Role.USER);
        
        // Create cart for admin
        Cart adminCart = new Cart();
        adminCart.setUser(adminUser);
        adminUser.setCart(adminCart);
        
        userRepository.save(adminUser);
        log.info("Admin user created: {}", adminUser.getUsername());

        // Create regular user
        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setEmail("user@example.com");
        regularUser.setPassword(passwordEncoder.encode("user123"));
        regularUser.setRoles(new HashSet<>());
        regularUser.getRoles().add(User.Role.USER);
        
        // Create cart for regular user
        Cart userCart = new Cart();
        userCart.setUser(regularUser);
        regularUser.setCart(userCart);
        
        userRepository.save(regularUser);
        log.info("Regular user created: {}", regularUser.getUsername());
    }

    private InitialDataDto loadInitialData() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/initial-data.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, InitialDataDto.class);
        }
    }

    private void initializeData(InitialDataDto initialData) {
        Map<String, Category> categoryMap = new HashMap<>();

        // Save categories
        initialData.getCategories().forEach(categoryDto -> {
            Category category = new Category();
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());
            
            Category savedCategory = categoryRepository.save(category);
            categoryMap.put(savedCategory.getName(), savedCategory);
            log.debug("Saved category: {}", savedCategory.getName());
        });

        // Save products
        initialData.getProducts().forEach(productDto -> {
            Category category = categoryMap.get(productDto.getCategoryName());
            
            if (category != null) {
                Product product = new Product();
                product.setName(productDto.getName());
                product.setDescription(productDto.getDescription());
                product.setPrice(productDto.getPrice() != null ? 
                        productDto.getPrice() : BigDecimal.ZERO);
                product.setStockQuantity(productDto.getStockQuantity() != null ? 
                        productDto.getStockQuantity() : 0);
                product.setCategory(category);
                
                productRepository.save(product);
                log.debug("Saved product: {}", product.getName());
            } else {
                log.warn("Category not found for product: {}", productDto.getName());
            }
        });
    }
} 