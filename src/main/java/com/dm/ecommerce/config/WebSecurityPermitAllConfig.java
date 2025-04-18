package com.dm.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Configuration to permit all requests to static resources.
 * This configuration has higher precedence than the main security config.
 */
@Configuration
@EnableWebSecurity
@Order(1) // Higher precedence than default (Order(100))
public class WebSecurityPermitAllConfig {

    /**
     * Configure a security filter chain for static resources.
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain permitAllSecurityFilterChain(HttpSecurity http) throws Exception {
        // Create a combined matcher for all static resources
        RequestMatcher staticResourcesMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/index.html"),
            new AntPathRequestMatcher("/css/**"),
            new AntPathRequestMatcher("/js/**"), 
            new AntPathRequestMatcher("/images/**"),
            new AntPathRequestMatcher("/favicon.ico"),
            new AntPathRequestMatcher("/error"),
            new AntPathRequestMatcher("/h2-console/**"),
            new AntPathRequestMatcher("/emergency"),
            new AntPathRequestMatcher("/direct-index")
        );
        
        http
            .securityMatcher(staticResourcesMatcher)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        
        return http.build();
    }
} 