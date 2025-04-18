package com.dm.ecommerce.service;

import com.dm.ecommerce.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User-related operations.
 */
public interface UserService {
    
    /**
     * Register a new user.
     *
     * @param username the username
     * @param email    the email
     * @param password the password
     * @return the created user
     */
    User registerUser(String username, String email, String password);
    
    /**
     * Find a user by username.
     *
     * @param username the username
     * @return an optional containing the user, if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email.
     *
     * @param email the email
     * @return an optional containing the user, if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by ID.
     *
     * @param id the user ID
     * @return an optional containing the user, if found
     */
    Optional<User> findById(Long id);
    
    /**
     * Get all users.
     *
     * @return a list of all users
     */
    List<User> findAll();
    
    /**
     * Update a user.
     *
     * @param id       the user ID
     * @param username the new username
     * @param email    the new email
     * @param password the new password
     * @return the updated user
     */
    User updateUser(Long id, String username, String email, String password);
    
    /**
     * Delete a user.
     *
     * @param id the user ID
     * @return true if the user was deleted, false otherwise
     */
    boolean deleteUser(Long id);
} 