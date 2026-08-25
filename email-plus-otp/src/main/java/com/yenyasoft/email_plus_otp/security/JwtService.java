package com.yenyasoft.email_plus_otp.security;

import com.yenyasoft.email_plus_otp.dtos.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Subash
 * Created: 8/25/2026
 * File: JwtService.java
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-issuer}")
    private String issuer;
    @Value("${app.jwt-access-expiry}")
    private Long accessExpiryMs;
    @Value("${app.jwt-refresh-expiry}")
    private Long refreshExpiryMs;

    private SecretKey generateSignINKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    //generate accessToken
    public String generateAccessToken(Authentication authentication){
        Map<String,Object> claims = new HashMap<>();
        claims.put("token_type","AccessToken");
        return generateToken(authentication, accessExpiryMs, claims);
    }
    //generate refreshToken
    public String generateRefreshToken(Authentication authentication){
        Map<String,Object> claims = new HashMap<>();
        claims.put("token_type","RefreshToken");
        return generateToken(authentication, refreshExpiryMs, claims);
    }

    public String generateToken(Authentication authentication, long expirations, Map<String, Object> claims){
        Date now = new Date();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(user.getUsername())
                .signWith(generateSignINKey())
                .issuer(issuer)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirations))
                .claims(claims)
                .compact();
    }
    //generateTokenPair
    public TokenPair getTokens(Authentication authentication){
        return new TokenPair(
                generateAccessToken(authentication),
                generateRefreshToken(authentication)
        );
    }
    public Claims extractAllClaims(String token){
        try{
            return Jwts.parser()
                    .verifyWith(generateSignINKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (Exception e){
            log.error("Exception occurred when parsing JWT signature: {}", e.getMessage());
        }
        return null;
    }
    public String extractUsername(String token){
        try{
            return extractAllClaims(token).getSubject();
        }catch (Exception e){
            log.error("Exception occurred when parsing JWT token: {}", e.getMessage());
        }
        return null;
    }
    public Boolean validateToken(String token, UserDetails user){
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject()!=null && claims.getSubject().equals(user.getUsername());
        }catch (Exception e){
            log.error("Exception occurred when validating JWT token: {}", e.getMessage());
        }
        return false;
    }
    public Boolean isTokenExpired(String token){
        try{
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().before(new Date());
        }
        catch (Exception e){
            log.error("Exception occurred when validating JWT token: {}", e.getMessage());
        }
        return false;
    }



}
