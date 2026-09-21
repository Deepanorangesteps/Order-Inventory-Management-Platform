package com.uphead.order_management.service;

import java.util.List;

import com.uphead.order_management.request.WarehouseRequest;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.response.WarehouseResponse;

public interface WarehouseService {
	
	 WarehouseResponse createWarehouse(WarehouseRequest request);

	    WarehouseResponse getWarehouseById(Long warehouseId);

	    List<WarehouseResponse> getAllWarehouses();

	    WarehouseResponse updateWarehouse(
	            Long warehouseId,
	            WarehouseRequest request
	    );

	    void deleteWarehouse(Long warehouseId);
	    
	    PageResponse<WarehouseResponse> getWarehouses(
	            int page,
	            int size,
	            String search
	    );
	    

}
