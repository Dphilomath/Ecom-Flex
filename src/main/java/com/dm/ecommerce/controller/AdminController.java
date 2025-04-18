package com.dm.ecommerce.controller;

import com.dm.ecommerce.model.User;
import com.dm.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for admin-specific operations.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;
    
    /**
     * Admin dashboard information.
     * 
     * @return admin dashboard data
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("message", "Welcome to Admin Dashboard");
        dashboard.put("status", "success");
        
        // Add some fake stats
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 42);
        stats.put("totalProducts", 156);
        stats.put("totalOrders", 327);
        stats.put("revenue", 15799.99);
        
        dashboard.put("stats", stats);
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * Get all users (admin only).
     * 
     * @return list of all users
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    /**
     * Delete a user (admin only).
     * 
     * @param id the user ID to delete
     * @return success/failure message
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            response.put("message", "User deleted successfully");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to delete user");
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }
} 