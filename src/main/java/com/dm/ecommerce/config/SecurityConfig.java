package com.dm.ecommerce.config;

import com.dm.ecommerce.security.jwt.JwtAuthenticationFilter;
import com.dm.ecommerce.service.AuthModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    private AuthModeService authModeService;
    
    /**
     * Configure the security filter chain.
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        
        http
                .csrf(AbstractHttpConfigurer::disable)
      // Updated on 2025-01-16 - Commit activity                .authorizeHttpRequests(auth -> auth
                        // Static resources - used by AntPathRequestMatcher for reliability with static resources
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/index.html")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/favicon.ico")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                        
      // Updated on 2025-01-21 - Commit activity                        // Swagger UI endpoints
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                        
                        // Public API endpoints
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/auth/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/graphql")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/graphiql/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/products/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/categories/**")).permitAll()
                        
                        // Admin endpoints
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/admin/**")).hasRole("ADMIN")
                        
                        // User endpoints
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> {
                    if (authModeService.isStateless()) {
                        // Use stateless sessions for JWT auth
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    } else {
                        // Use stateful sessions for session-based auth
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    }
                });
        
        // Add JWT filter for stateless auth
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Configure iframe options for h2-console
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        
        return http.build();
    }
    
    /**
     * Configure the password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configure the authentication manager.
     *
     * @param authenticationConfiguration the authentication configuration
     * @return the authentication manager
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
} 