package com.uphead.order_management.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.request.OrganizationRequest;
import com.uphead.order_management.response.ApiResponse;
import com.uphead.order_management.service.OrganizationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {
	
	 private final OrganizationService organizationService;

	    public OrganizationController(OrganizationService organizationService) {
	        this.organizationService = organizationService;
	    }
	    

	    @PostMapping(
	    	    consumes = MediaType.APPLICATION_JSON_VALUE,
	    	    produces = MediaType.APPLICATION_JSON_VALUE
	    	)
	    public ResponseEntity<ApiResponse<Organization>> createOrganization(
	             @RequestBody OrganizationRequest organization) {

	    	System.out.println("Request : "+organization);
	        Organization savedOrganization =
	                organizationService.createOrganization(organization);
	        ApiResponse<Organization> response = new ApiResponse<>(
	                true,
	                "Organization created successfully",
	                savedOrganization
	        );

	        return ResponseEntity.ok(response);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Organization> getOrganization(
	            @PathVariable Long id) {

	        return ResponseEntity.ok(
	                organizationService.getOrganizationById(id)
	        );
	    }

	    @GetMapping
	    public ResponseEntity<List<Organization>> getAllOrganizations() {

	        return ResponseEntity.ok(
	                organizationService.getAllOrganizations()
	        );
	    }

}
