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

import com.uphead.order_management.request.ProductRequest;
import com.uphead.order_management.response.ApiResponse;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.response.ProductResponse;
import com.uphead.order_management.service.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
	
	private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
    	ProductResponse response = productService.createProduct(request);
    	return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                                true,
                                "Product created successfully",
                                response
                        )
                );
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @PathVariable Long id) {
    	
    	ProductResponse product = productService.getProductById(id);
    	return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product retrieved successfully",
                        product
                )
        );
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
    	
    	List<ProductResponse> productList = productService.getAllProducts();
    	return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Products retrieved successfully",
                        productList
                )
        );
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
    	
    	ProductResponse productResposne = productService.updateProduct(id, request);
    	return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product updated successfully",
                        productResposne
                )
        );
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id) {
    	
    	productService.deleteProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product deleted successfully",
                        null
                )
        );
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {

        PageResponse<ProductResponse> response =
                productService.getProducts(
                        page,
                        size,
                        search
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Products retrieved successfully",
                        response
                )
        );
    }

}
