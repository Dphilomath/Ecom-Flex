package com.dm.ecommerce.config;

import com.dm.ecommerce.model.User;
import com.dm.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    
    @Override
    public void run(String... args) throws Exception {
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
} 