package com.uphead.order_management.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class WarehouseRequest {
	

    @NotBlank(message = "Warehouse name is required")
    @Size( max = 150, message = "Warehouse name must not exceed 150 characters"
    )
    private String name;

    @NotBlank(message = "Warehouse code is required")
    @Size( max = 100,message = "Warehouse code must not exceed 100 characters"
    )
    private String code;

    @Size( max = 500, message = "Address must not exceed 500 characters"
    )
    private String address;

    private Boolean active = true;

    public WarehouseRequest() {
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

	@Override
	public String toString() {
		return "WarehouseRequest [name=" + name + ", code=" + code + ", address=" + address + ", active=" + active
				+ "]";
	}
    
    

}
