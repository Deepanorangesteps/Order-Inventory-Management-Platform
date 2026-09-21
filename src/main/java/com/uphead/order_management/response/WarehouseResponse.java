package com.uphead.order_management.response;

import java.time.LocalDateTime;

public class WarehouseResponse {
	private Long warehouseId;
    private Long organizationId;
    private String name;
    private String code;
    private String address;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WarehouseResponse() {
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

	@Override
	public String toString() {
		return "WarehouseResponse [warehouseId=" + warehouseId + ", organizationId=" + organizationId + ", name=" + name
				+ ", code=" + code + ", address=" + address + ", active=" + active + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
    
     

}
