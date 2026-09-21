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

import com.uphead.order_management.request.WarehouseRequest;
import com.uphead.order_management.response.ApiResponse;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.response.WarehouseResponse;
import com.uphead.order_management.service.WarehouseService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {
	
	 private final WarehouseService warehouseService;

	    public WarehouseController(
	            WarehouseService warehouseService) {
	        this.warehouseService = warehouseService;
	    }
	    
	    @PostMapping
	    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	    public ResponseEntity<ApiResponse<WarehouseResponse>>createWarehouse(
	                    @Valid @RequestBody WarehouseRequest request) {
	    	
	    	WarehouseResponse wareHouse = warehouseService.createWarehouse(request);
	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(
	                        new ApiResponse<>(
	                                true,
	                                "Warehouse created successfully",
	                                wareHouse
	                        )
	                );
	    }
	    
	    @GetMapping("/{warehouseId}")
	    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	    public ResponseEntity<ApiResponse<WarehouseResponse>>getWarehouse(@PathVariable Long warehouseId) {

	        WarehouseResponse response = warehouseService.getWarehouseById(  warehouseId );

	        return ResponseEntity.ok(
	                new ApiResponse<>(
	                        true,
	                        "Warehouse retrieved successfully",
	                        response
	                )
	        );
	    }

	    @GetMapping
	    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	    public ResponseEntity<ApiResponse<List<WarehouseResponse>>>getAllWarehouses() {
	        List<WarehouseResponse> response =
	                warehouseService.getAllWarehouses();

	        return ResponseEntity.ok(
	                new ApiResponse<>(
	                        true,
	                        "Warehouses retrieved successfully",
	                        response
	                )
	        );
	    }

	    @PutMapping("/{warehouseId}")
	    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	    public ResponseEntity<ApiResponse<WarehouseResponse>>updateWarehouse(@PathVariable Long warehouseId,
	                    @Valid @RequestBody WarehouseRequest request) {

	        WarehouseResponse response =
	                warehouseService.updateWarehouse(
	                        warehouseId,
	                        request
	                );

	        return ResponseEntity.ok(
	                new ApiResponse<>(
	                        true,
	                        "Warehouse updated successfully",
	                        response
	                )
	        );
	    }

	    @DeleteMapping("/{warehouseId}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<ApiResponse<Void>>deleteWarehouse(@PathVariable Long warehouseId) {

	        warehouseService.deleteWarehouse(
	                warehouseId
	        );

	        return ResponseEntity.ok(
	                new ApiResponse<>(
	                        true,
	                        "Warehouse deleted successfully",
	                        null
	                )
	        );
	    }

	    @GetMapping("/search")
	    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	    public ResponseEntity<ApiResponse<PageResponse<WarehouseResponse>>> getWarehouses(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(required = false) String search
	    ) {

	        PageResponse<WarehouseResponse> response =
	                warehouseService.getWarehouses(
	                        page,
	                        size,
	                        search
	                );

	        return ResponseEntity.ok(
	                new ApiResponse<>(
	                        true,
	                        "Warehouses retrieved successfully",
	                        response
	                )
	        );
	    }
}
