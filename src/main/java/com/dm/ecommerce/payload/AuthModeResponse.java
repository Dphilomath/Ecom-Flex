package com.dm.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload for authentication mode operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthModeResponse {
    private String currentMode;
} 