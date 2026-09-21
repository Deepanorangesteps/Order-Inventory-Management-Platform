package com.uphead.order_management.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {
	  @NotNull(message = "Customer ID is required")
	    private Long customerId;

	    @NotEmpty(message = "Order must contain at least one item")
	    @Valid
	    private List<OrderItemRequest> items;

	    public Long getCustomerId() {
	        return customerId;
	    }

	    public void setCustomerId(Long customerId) {
	        this.customerId = customerId;
	    }

	    public List<OrderItemRequest> getItems() {
	        return items;
	    }

	    public void setItems(List<OrderItemRequest> items) {
	        this.items = items;
	    }

}
