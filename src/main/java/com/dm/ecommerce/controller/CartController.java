package com.dm.ecommerce.controller;

import com.dm.ecommerce.model.Cart;
import com.dm.ecommerce.model.CartItem;
import com.dm.ecommerce.model.Product;
import com.dm.ecommerce.model.User;
import com.dm.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Cart operations
 */
@RestController
@RequestMapping("/api/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get the current user's cart
     *
     * @return the user's cart
     */
    @GetMapping
    public ResponseEntity<Cart> getCart() {
        User user = getCurrentUser();
        
        // In a real application, we would fetch the cart from a repository
        // For now, we'll create a dummy cart for demonstration
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }
        
        return ResponseEntity.ok(cart);
    }

    /**
     * Add a product to the cart
     *
     * @param productId the product ID to add
     * @param quantity the quantity to add
     * @return the updated cart
     */
    @PostMapping("/items")
    public ResponseEntity<?> addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        
        if (quantity <= 0) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Quantity must be greater than zero");
            return ResponseEntity.badRequest().body(response);
        }
        
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Product product = productOpt.get();
        User user = getCurrentUser();
        
        // In a real application, we would fetch the cart from a repository
        // For now, we'll create a dummy response for demonstration
        Map<String, Object> response = new HashMap<>();
        response.put("message", quantity + " " + product.getName() + " added to cart");
        response.put("productId", productId);
        response.put("quantity", quantity);
        response.put("productName", product.getName());
        response.put("price", product.getPrice());
        response.put("subtotal", product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update the quantity of a cart item
     *
     * @param itemId the cart item ID
     * @param quantity the new quantity
     * @return the updated cart
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        
        if (quantity <= 0) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Quantity must be greater than zero");
            return ResponseEntity.badRequest().body(response);
        }
        
        // In a real application, we would fetch and update the cart item
        // For now, we'll create a dummy response for demonstration
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart item updated successfully");
        response.put("itemId", itemId);
        response.put("quantity", quantity);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Remove an item from the cart
     *
     * @param itemId the cart item ID to remove
     * @return no content if successful
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long itemId) {
        // In a real application, we would fetch and remove the cart item
        // For now, we'll just return a successful response
        return ResponseEntity.noContent().build();
    }

    /**
     * Clear the cart
     *
     * @return the empty cart
     */
    @DeleteMapping
    public ResponseEntity<?> clearCart() {
        // In a real application, we would fetch and clear the user's cart
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Helper method to get the current authenticated user
     * 
     * @return the current user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // In a real application, we would fetch the user from a UserService or UserRepository
        // For now, we'll create a dummy user for demonstration
        User user = new User();
        user.setId(1L);
        user.setUsername(authentication.getName());
        return user;
    }
} 