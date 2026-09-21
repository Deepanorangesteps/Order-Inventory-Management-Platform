package com.uphead.order_management.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
	
	 public AuthenticatedUser getCurrentUser() {

	        Authentication authentication =
	                SecurityContextHolder
	                        .getContext()
	                        .getAuthentication();

	        if (authentication == null ||
	                !authentication.isAuthenticated()) {

	            throw new AuthenticationCredentialsNotFoundException(
	                    "User is not authenticated"
	            );
	        }

	        Object principal = authentication.getPrincipal();

	        if (!(principal instanceof AuthenticatedUser)) {

	            throw new AuthenticationCredentialsNotFoundException(
	                    "Invalid authenticated user"
	            );
	        }

	        return (AuthenticatedUser) principal;
	    }

	    public Long getCurrentUserId() {
	        return getCurrentUser().getUserId();
	    }

	    public Long getCurrentOrganizationId() {
	        return getCurrentUser().getOrganizationId();
	    }

	    public String getCurrentUserEmail() {
	        return getCurrentUser().getEmail();
	    }

	    public String getCurrentUserRole() {
	        return getCurrentUser().getRole();
	    }

}
