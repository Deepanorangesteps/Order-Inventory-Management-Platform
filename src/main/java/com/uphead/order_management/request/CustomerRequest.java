package com.uphead.order_management.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerRequest {
	
	   @NotBlank(message = "Customer name is required")
	    @Size(max = 150, message = "Customer name must not exceed 150 characters")
	    private String name;

	    @NotBlank(message = "Email is required")
	    @Email(message = "Invalid email format")
	    @Size(max = 255, message = "Email must not exceed 255 characters")
	    private String email;

	    @Size(max = 30, message = "Phone must not exceed 30 characters")
	    private String phone;

	    @Size(max = 500, message = "Address must not exceed 500 characters")
	    private String address;

	    private Boolean active = true;

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
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

}
