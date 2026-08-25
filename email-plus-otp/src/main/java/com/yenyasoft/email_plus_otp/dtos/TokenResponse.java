package com.yenyasoft.email_plus_otp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Subash
 * Created: 8/25/2026
 * File: TokenResponse.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    private TokenPair tokenPair;
    private UserResponse user;
}
