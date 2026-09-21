package com.uphead.order_management.security;

public class AuthenticatedUser {
	
	 private final Long userId;
	    private final Long organizationId;
	    private final String email;
	    private final String role;

	    public AuthenticatedUser(
	            Long userId,
	            Long organizationId,
	            String email,
	            String role) {

	        this.userId = userId;
	        this.organizationId = organizationId;
	        this.email = email;
	        this.role = role;
	    }

	    public Long getUserId() {
	        return userId;
	    }

	    public Long getOrganizationId() {
	        return organizationId;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public String getRole() {
	        return role;
	    }

}
