package com.uphead.order_management.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.exception.ResourceNotFoundException;
import com.uphead.order_management.repository.OrganizationRepository;
import com.uphead.order_management.request.OrganizationRequest;
import com.uphead.order_management.service.OrganizationService;

@Service
public class OrganizationServiceImpl implements OrganizationService{
	
	private OrganizationRepository organizationRepository;
	public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

	@Override
	public Organization createOrganization(OrganizationRequest organizationRequest) {
		if (organizationRepository.existsByCode(organizationRequest.getCode())) {
            throw new RuntimeException("Organization code already exists");
        }

        if (organizationRepository.existsByName(organizationRequest.getName())) {
            throw new RuntimeException("Organization name already exists");
        }
        Organization organization = new Organization();
        organization.setCode(organizationRequest.getCode());
        organization.setName(organizationRequest.getName());
        organization.setActive(true);

        return organizationRepository.save(organization);
	}

	@Override
	public Organization getOrganizationById(Long id) {
		try {
			return organizationRepository.findById(id)
			        .orElseThrow(() ->
			                new ResourceNotFoundException("Organization not found"));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Organization> getAllOrganizations() {
		 return organizationRepository.findAll();
	}

}
