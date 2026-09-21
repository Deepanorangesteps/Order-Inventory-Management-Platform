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

import com.uphead.order_management.request.InventoryRequest;
import com.uphead.order_management.response.ApiResponse;
import com.uphead.order_management.response.InventoryResponse;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.service.InventoryService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
	private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService) {

        this.inventoryService = inventoryService;
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>>createInventory(
                    @Valid @RequestBody InventoryRequest request) {
    	
    	InventoryResponse inveResponse = inventoryService.createInventory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Inventory created successfully",
                                inveResponse
                        )
                );
    }
    
    @GetMapping("/{inventoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventory(
                    @PathVariable Long inventoryId) {
    	
    	InventoryResponse inveResponse = inventoryService.getInventoryById(inventoryId);
    	 return ResponseEntity.ok(
                 new ApiResponse<>(
                         true,
                         "Inventory retrieved successfully",
                         inveResponse
                 )
         );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAllInventory() {
    	
    	List<InventoryResponse> inventoryResList = inventoryService.getAllInventory();
    	 return ResponseEntity.ok(
                 new ApiResponse<>(
                         true,
                         "Inventory retrieved successfully",
                         inventoryResList
                 )
         );
    }
    
    @PutMapping("/{inventoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateInventory(@PathVariable Long inventoryId,
                    @Valid @RequestBody InventoryRequest request) {
    	
    	InventoryResponse inventory = inventoryService.updateInventory(inventoryId, request);
    	return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Inventory updated successfully",
                        inventory
                )
        );
    }
    @DeleteMapping("/{inventoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteInventory(
                    @PathVariable Long inventoryId) {

        inventoryService.deleteInventory(
                inventoryId
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Inventory deleted successfully",
                        null
                )
        );
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<InventoryResponse>>> getInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId
    ) {

        PageResponse<InventoryResponse> response =
                inventoryService.getInventory(
                        page,
                        size,
                        productId,
                        warehouseId
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Inventory retrieved successfully",
                        response
                )
        );
    }


}
