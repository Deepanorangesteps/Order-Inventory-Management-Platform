package com.uphead.order_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uphead.order_management.request.CustomerRequest;
import com.uphead.order_management.response.ApiResponse;
import com.uphead.order_management.response.CustomerResponse;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.service.CustomerService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
        @Valid @RequestBody CustomerRequest request) {
    	CustomerResponse response = customerService.createCustomer(request);
    	  return ResponseEntity
    	            .status(HttpStatus.CREATED)
    	            .body(
    	                new ApiResponse<>(
    	                    true,
    	                    "Customer created successfully",
    	                    response
    	                )
    	            );
    }
    
    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(
        @PathVariable Long customerId ) {
    	CustomerResponse customer = customerService.getCustomer(customerId);
    	 return ResponseEntity.ok(
    	            new ApiResponse<>(
    	                true,
    	                "Customer fetched successfully",
    	                customer
    	            )
    	        );
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {

        List<CustomerResponse> response =
            customerService.getAllCustomers();

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Customers fetched successfully",
                response
            )
        );
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(@PathVariable Long customerId,
        @Valid @RequestBody CustomerRequest request
    ) {
    	CustomerResponse response  =customerService.updateCustomer(customerId, request);
    	 return ResponseEntity.ok(
    	            new ApiResponse<>(
    	                true,
    	                "Customer updated successfully",
    	                response
    	            )
    	        );
    	
    }
    
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer( @PathVariable Long customerId
    ) {
    	customerService.deleteCustomer(customerId);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Customer deleted successfully",
                null
            )
        );
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        PageResponse<CustomerResponse> response =
                customerService.getCustomers(
                        page,
                        size,
                        search
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Customers retrieved successfully",
                        response
                )
        );
    }
}
