package com.uphead.order_management.response;

import java.time.LocalDateTime;

public class CustomerResponse {
	
	 private Long customerId;
	    private Long organizationId;
	    private String name;
	    private String email;
	    private String phone;
	    private String address;
	    private Boolean active;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
		public Long getCustomerId() {
			return customerId;
		}
		public void setCustomerId(Long customerId) {
			this.customerId = customerId;
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
	    
	    

}
