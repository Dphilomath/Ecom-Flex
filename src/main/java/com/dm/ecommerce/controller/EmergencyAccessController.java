package com.dm.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Emergency controller to test access when other approaches fail.
 */
@RestController
public class EmergencyAccessController {

    /**
     * Simple HTML test page that should be accessible without authentication.
     * 
     * @return HTML content
     */
    @GetMapping(value = "/emergency", produces = "text/html")
    public String emergencyAccess() {
        return "<html><body>" +
                "<h1>Emergency Access Page</h1>" +
                "<p>If you can see this page, the basic security setup is working correctly.</p>" +
                "<p>Try the <a href='/'>main page</a> now.</p>" +
                "</body></html>";
    }
} 