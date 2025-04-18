package com.dm.ecommerce.graphql;

import com.dm.ecommerce.config.AuthMode;
import com.dm.ecommerce.model.User;
import com.dm.ecommerce.payload.AuthModeResponse;
import com.dm.ecommerce.payload.AuthResponse;
import com.dm.ecommerce.payload.LoginRequest;
import com.dm.ecommerce.payload.RegisterRequest;
import com.dm.ecommerce.security.jwt.JwtUtils;
import com.dm.ecommerce.service.AuthModeService;
import com.dm.ecommerce.service.UserService;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * GraphQL resolver for authentication-related operations.
 */
@Controller
public class AuthResolver {
    
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
     * @param user the user data
     * @return the authentication response
     */
    @MutationMapping
    public AuthResponse registerUser(@Argument("user") RegisterRequest user) {
        User registeredUser = userService.registerUser(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
        
        String jwt = null;
        if (authModeService.isStateless()) {
            jwt = jwtUtils.generateJwtToken(registeredUser);
        }
        
        return new AuthResponse(jwt, registeredUser);
    }
    
    /**
     * Authenticate a user.
     *
     * @param login the login data
     * @return the authentication response
     */
    @MutationMapping
    public AuthResponse login(@Argument("login") LoginRequest login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getUsername(),
                        login.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userService.findByUsername(login.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String jwt = null;
        if (authModeService.isStateless()) {
            jwt = jwtUtils.generateJwtToken(authentication);
        }
        
        return new AuthResponse(jwt, user);
    }
    
    /**
     * Log out a user.
     *
     * @return true if logout was successful
     */
    @MutationMapping
    public Boolean logout() {
        if (authModeService.isStateful()) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
            }
        }
        
        SecurityContextHolder.clearContext();
        return true;
    }
    
    /**
     * Get the current authentication mode.
     *
     * @return the authentication mode
     */
    @QueryMapping
    public AuthModeResponse getCurrentAuthMode() {
        return new AuthModeResponse(authModeService.getCurrentMode().name());
    }
    
    /**
     * Switch the authentication mode.
     *
     * @param mode the new authentication mode
     * @return the updated authentication mode
     */
    @MutationMapping
    public AuthModeResponse switchAuthMode(@Argument String mode) {
        AuthMode newMode = AuthMode.valueOf(mode.toUpperCase());
        authModeService.switchMode(newMode);
        return new AuthModeResponse(newMode.name());
    }
    
    /**
     * Get the current authenticated user.
     *
     * @param env the data fetching environment
     * @return the current user, or null if not authenticated
     */
    @QueryMapping
    public User getCurrentUser(DataFetchingEnvironment env) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        
        return userService.findByUsername(authentication.getName())
                .orElse(null);
    }
} 