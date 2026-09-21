package com.uphead.order_management.service;

import java.util.List;

import com.uphead.order_management.request.InventoryRequest;
import com.uphead.order_management.response.InventoryResponse;
import com.uphead.order_management.response.PageResponse;

public interface InventoryService {
	InventoryResponse createInventory(InventoryRequest request);

    InventoryResponse getInventoryById(Long inventoryId);

    List<InventoryResponse> getAllInventory();

    InventoryResponse updateInventory(
            Long inventoryId,
            InventoryRequest request
    );

    void deleteInventory(Long inventoryId);
    
    PageResponse<InventoryResponse> getInventory(
            int page,
            int size,
            Long productId,
            Long warehouseId
    );

}
