package com.dm.ecommerce.service;

import com.dm.ecommerce.config.AuthMode;
import org.springframework.stereotype.Service;

/**
 * Service responsible for managing the authentication mode.
 */
@Service
public class AuthModeService {
    
    private AuthMode currentMode = AuthMode.STATELESS; // Default to JWT-based auth
    
    /**
     * Get the current authentication mode.
     *
     * @return the current authentication mode
     */
    public AuthMode getCurrentMode() {
        return currentMode;
    }
    
    /**
     * Switch the authentication mode.
     *
     * @param mode the new authentication mode
     * @return the updated authentication mode
     */
    public AuthMode switchMode(AuthMode mode) {
        this.currentMode = mode;
        return this.currentMode;
    }
    
    /**
     * Check if the current authentication mode is stateless (JWT-based).
     *
     * @return true if the current mode is stateless, false otherwise
     */
    public boolean isStateless() {
        return currentMode == AuthMode.STATELESS;
    }
    
    /**
     * Check if the current authentication mode is stateful (session-based).
     *
     * @return true if the current mode is stateful, false otherwise
     */
    public boolean isStateful() {
        return currentMode == AuthMode.STATEFUL;
    }
} 