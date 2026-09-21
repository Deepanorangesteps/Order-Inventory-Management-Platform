package com.uphead.order_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uphead.order__management.auth.AuthService;
import com.uphead.order_management.request.LoginRequest;
import com.uphead.order_management.request.RefreshTokenRequest;
import com.uphead.order_management.response.ApiResponse;
import com.uphead.order_management.response.LoginResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Login successful",
                        response
                )
        );
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        LoginResponse response =
                authService.refreshAccessToken(
                        request.getRefreshToken()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Access token refreshed successfully",
                        response
                )
        );
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request) {

        authService.logout(request.getRefreshToken());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Logout successful",
                        null
                )
        );
    }

}
