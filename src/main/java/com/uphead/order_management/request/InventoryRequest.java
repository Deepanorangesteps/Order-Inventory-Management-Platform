package com.uphead.order_management.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventoryRequest {
	 @NotNull(message = "Product ID is required")
	    private Long productId;

	    @NotNull(message = "Warehouse ID is required")
	    private Long warehouseId;

	    @NotNull(message = "Quantity is required")
	    @Min(value = 0, message = "Quantity cannot be negative")
	    private Integer quantity;

	    public InventoryRequest() {
	    }

	    public Long getProductId() {
	        return productId;
	    }

	    public void setProductId(Long productId) {
	        this.productId = productId;
	    }

	    public Long getWarehouseId() {
	        return warehouseId;
	    }

	    public void setWarehouseId(Long warehouseId) {
	        this.warehouseId = warehouseId;
	    }

	    public Integer getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }
	

}
