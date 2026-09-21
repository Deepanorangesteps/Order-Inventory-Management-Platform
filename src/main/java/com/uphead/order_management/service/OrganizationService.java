package com.uphead.order_management.service;

import java.util.List;

import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.request.OrganizationRequest;

public interface OrganizationService {
	

    Organization createOrganization(OrganizationRequest organizationRequest);

    Organization getOrganizationById(Long id);

    List<Organization> getAllOrganizations();

}
