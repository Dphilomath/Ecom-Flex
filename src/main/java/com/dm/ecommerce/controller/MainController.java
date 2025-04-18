package com.dm.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for main application routes.
 */
@Controller
public class MainController {

    /**
     * Handles the root URL and returns the index page.
     * 
     * @return redirect to the index.html page
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }
} 