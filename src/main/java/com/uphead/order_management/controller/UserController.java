package com.uphead.order_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uphead.order_management.entity.User;
import com.uphead.order_management.request.UserRequest;
import com.uphead.order_management.response.ApiResponse;
import com.uphead.order_management.response.UserResponse;
import com.uphead.order_management.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	   private final UserService userService;

	    public UserController(UserService userService) {
	        this.userService = userService;
	    }

	    @PostMapping
	    public ResponseEntity<ApiResponse<User>> createUser(
	            @Valid @RequestBody UserRequest request) {

	        User response =
	                userService.createUser(request);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(
	                        new ApiResponse<>(
	                                true,
	                                "User created successfully",
	                                response
	                        )
	                );
	    }
	    

	    @GetMapping("/{id}")
	    public ResponseEntity<ApiResponse<UserResponse>> getUser(
	            @PathVariable Long id) {

	        UserResponse response =
	                userService.getUserById(id);

	        return ResponseEntity.ok(
	                new ApiResponse<>(
	                        true,
	                        "User retrieved successfully",
	                        response
	                )
	        );
	    }

	    @GetMapping
	    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersList(
	            ) {

	        List<UserResponse> response =
	                userService.getAllUsers();

	        return ResponseEntity.ok(
	                new ApiResponse<>(
	                        true,
	                        "Users List retrieved successfully",
	                        response
	                )
	        );
	    }

}
