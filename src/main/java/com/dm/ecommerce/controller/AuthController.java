package com.dm.ecommerce.controller;

import com.dm.ecommerce.config.AuthMode;
import com.dm.ecommerce.model.User;
import com.dm.ecommerce.payload.AuthModeResponse;
import com.dm.ecommerce.payload.AuthResponse;
import com.dm.ecommerce.payload.LoginRequest;
import com.dm.ecommerce.payload.RegisterRequest;
import com.dm.ecommerce.security.jwt.JwtUtils;
import com.dm.ecommerce.service.AuthModeService;
import com.dm.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private AuthModeService authModeService;
    
    /**
     * Register a new user.
     *
     * @param registerRequest the registration request
     * @return the authentication response
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        User user = userService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );
        
        String jwt = null;
        if (authModeService.isStateless()) {
            jwt = jwtUtils.generateJwtToken(user);
        }
        
        return ResponseEntity.ok(new AuthResponse(jwt, user));
    }
    
    /**
     * Authenticate a user.
     *
     * @param loginRequest the login request
     * @return the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String jwt = null;
        if (authModeService.isStateless()) {
            jwt = jwtUtils.generateJwtToken(authentication);
        }
        
        return ResponseEntity.ok(new AuthResponse(jwt, user));
    }
    
    /**
     * Logout a user.
     *
     * @param request the HTTP request
     * @return a response indicating success
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        if (authModeService.isStateful()) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
    
    /**
     * Get the current authentication mode.
     *
     * @return the authentication mode response
     */
    @GetMapping("/mode")
    public ResponseEntity<AuthModeResponse> getAuthMode() {
        return ResponseEntity.ok(new AuthModeResponse(authModeService.getCurrentMode().name()));
    }
    
    /**
     * Switch the authentication mode.
     *
     * @param mode the new authentication mode
     * @return the updated authentication mode response
     */
    @PostMapping("/mode")
    public ResponseEntity<AuthModeResponse> switchAuthMode(@RequestParam String mode) {
        AuthMode newMode = AuthMode.valueOf(mode.toUpperCase());
        authModeService.switchMode(newMode);
        return ResponseEntity.ok(new AuthModeResponse(newMode.name()));
    }
} 