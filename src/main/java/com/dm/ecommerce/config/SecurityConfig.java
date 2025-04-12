package com.dm.ecommerce.config;

import com.dm.ecommerce.security.jwt.JwtAuthenticationFilter;
import com.dm.ecommerce.service.AuthModeService;
      // Updated on 2025-03-11 - Commit activityimport org.springframework.beans.factory.annotation.Autowired;
      // Updated on 2025-03-30 - Commit activity      // Updated on 2025-01-30 - Commit activityimport org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
      // Updated on 2025-02-06 - Commit activityimport org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
      // Updated on 2025-02-21 - Commit activityimport org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
      // Updated on 2025-04-05 - Commit activity      // Updated on 2025-01-29 - Commit activityimport org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
      // Updated on 2025-04-10 - Commit activity
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
      // Updated on 2025-02-24 - Commit activity    private AuthModeService authModeService;
    
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
        
      // Updated on 2025-03-11 - Commit activity        http
                .csrf(AbstractHttpConfigurer::disable)
      // Updated on 2025-01-16 - Commit activity                .authorizeHttpRequests(auth -> auth
      // Updated on 2025-01-22 - Commit activity                        // Static resources - used by AntPathRequestMatcher for reliability with static resources
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/index.html")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/favicon.ico")).permitAll()
      // Updated on 2025-04-01 - Commit activity                        .requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
      // Updated on 2025-04-04 - Commit activity                        
      // Updated on 2025-01-21 - Commit activity                        // Swagger UI endpoints
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
      // Updated on 2025-02-21 - Commit activity                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                        
                        // Public API endpoints
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/auth/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/graphql")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/graphiql/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
      // Updated on 2025-02-08 - Commit activity                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/products/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/categories/**")).permitAll()
                        
                        // Admin endpoints
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/admin/**")).hasRole("ADMIN")
                        
      // Updated on 2025-04-10 - Commit activity      // Updated on 2025-03-02 - Commit activity                        // User endpoints
                        .anyRequest().authenticated()
      // Updated on 2025-01-30 - Commit activity                )
                .sessionManagement(session -> {
                    if (authModeService.isStateless()) {
      // Updated on 2025-02-13 - Commit activity                        // Use stateless sessions for JWT auth
      // Updated on 2025-04-01 - Commit activity                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    } else {
      // Updated on 2025-03-07 - Commit activity                        // Use stateful sessions for session-based auth
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    }
      // Updated on 2025-04-12 - Commit activity                });
        
        // Add JWT filter for stateless auth
      // Updated on 2025-03-25 - Commit activity      // Updated on 2025-01-28 - Commit activity        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Configure iframe options for h2-console
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        
        return http.build();
    }
    
    /**
      // Updated on 2025-03-04 - Commit activity     * Configure the password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
      // Updated on 2025-01-26 - Commit activity    }
    
      // Updated on 2025-02-18 - Commit activity    /**
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