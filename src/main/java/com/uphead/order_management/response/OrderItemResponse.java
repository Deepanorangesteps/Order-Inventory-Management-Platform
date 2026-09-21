package com.uphead.order_management.response;

import java.math.BigDecimal;

public class OrderItemResponse {
	
	   private Long orderItemId;
	    private Long productId;
	    private Long warehouseId;
	    private Integer quantity;
	    private BigDecimal unitPrice;
	    private BigDecimal subtotal;

	    public Long getOrderItemId() {
	        return orderItemId;
	    }

	    public void setOrderItemId(Long orderItemId) {
	        this.orderItemId = orderItemId;
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

	    public BigDecimal getUnitPrice() {
	        return unitPrice;
	    }

	    public void setUnitPrice(BigDecimal unitPrice) {
	        this.unitPrice = unitPrice;
	    }

	    public BigDecimal getSubtotal() {
	        return subtotal;
	    }

	    public void setSubtotal(BigDecimal subtotal) {
	        this.subtotal = subtotal;
	    }

}
