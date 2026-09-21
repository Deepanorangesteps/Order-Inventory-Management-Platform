package com.uphead.order_management.security;

import org.springframework.stereotype.Service;

import com.uphead.order_management.entity.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	 private final Key signingKey;
	    private final long accessTokenExpiration;

	    public JwtService(
	            @Value("${jwt.secret}") String secret,
	            @Value("${jwt.access-token-expiration}") long accessTokenExpiration) {

	        this.signingKey = Keys.hmacShaKeyFor(
	                secret.getBytes(StandardCharsets.UTF_8)
	        );

	        this.accessTokenExpiration = accessTokenExpiration;
	    }
	    
	    public String generateAccessToken(User user) {

	        Date now = new Date();
	        Date expiration = new Date(
	                now.getTime() + accessTokenExpiration
	        );

	        return Jwts.builder()
	                .subject(user.getEmail())
	                .claim("userId", user.getUserId())
	                .claim("role", user.getRole().name())
	                .claim("organizationId", user.getOrganization().getOrganizationId())
	                .issuedAt(now)
	                .expiration(expiration)
	                .signWith(signingKey)
	                .compact();
	    }

	    public String extractUsername(String token) {
	        return extractAllClaims(token).getSubject();
	    }

	    public Long extractUserId(String token) {
	        Number userId = extractAllClaims(token).get("userId", Number.class);
	        return userId.longValue();
	    }

	    public Long extractOrganizationId(String token) {
	        Number organizationId =
	                extractAllClaims(token).get("organizationId", Number.class);

	        return organizationId.longValue();
	    }

	    public String extractRole(String token) {
	        return extractAllClaims(token).get("role", String.class);
	    }

	    public boolean isTokenValid(String token) {

	        try {
	            extractAllClaims(token);
	            return true;
	        } catch (Exception exception) {
	            return false;
	        }
	    }

	    public boolean isTokenExpired(String token) {

	        try {
	            Date expiration = extractAllClaims(token).getExpiration();
	            return expiration.before(new Date());
	        } catch (Exception exception) {
	            return true;
	        }
	    }

	    private Claims extractAllClaims(String token) {

	        return Jwts.parser()
	                .verifyWith((javax.crypto.SecretKey) signingKey)
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	    }

	    public long getAccessTokenExpiration() {
	        return accessTokenExpiration;
	    }

}
